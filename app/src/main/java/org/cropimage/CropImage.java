/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cropimage;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.media.ExifInterface;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.yksj.consultation.son.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

/**
 * The activity can crop specific region of interest from an image.
 */
public class CropImage extends MonitoredActivity {
	private static final String TAG = "CropImage";
	public static final int CROP_MSG = 10;
	public static final int CROP_MSG_INTERNAL = 100;

	// 输出格式
	private Bitmap.CompressFormat mOutputFormat = Bitmap.CompressFormat.JPEG; // only
																				// used
	private Uri mSaveUri = null;
	private int mAspectX, mAspectY; // CR: two definitions per line == sad
									// panda.
	private boolean mDoFaceDetection = false;
	private boolean mCircleCrop = false;
	private final Handler mHandler = new Handler();

	// These options specifiy the output image size and whether we should
	// scale the output to fit it (or just crop it).
	private int mOutputX, mOutputY;
	private boolean mScale;
	private boolean mScaleUp = true;

	boolean mWaitingToPick; // Whether we are wait the user to pick a face.
	boolean mSaving; // Whether the "save" button is already clicked.

	private CropImageView mImageView;
	private ContentResolver mContentResolver;

	private Bitmap mBitmap;

	HighlightView mCrop;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		mContentResolver = getContentResolver();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cropimage_layout);

		mImageView = (CropImageView) findViewById(R.id.image);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();

		if (extras != null) {
			if (extras.getString("circleCrop") != null) {
				mCircleCrop = true;
				mAspectX = 1;
				mAspectY = 1;
			}
			mSaveUri = (Uri) extras.getParcelable(MediaStore.EXTRA_OUTPUT);
			if (mSaveUri != null) {
				String outputFormatString = extras.getString("outputFormat");
				if (outputFormatString != null) {
					mOutputFormat = Bitmap.CompressFormat
							.valueOf(outputFormatString);
				}
			}
			mBitmap = (Bitmap) extras.getParcelable("data");
			mAspectX = extras.getInt("aspectX");
			mAspectY = extras.getInt("aspectY");
			mOutputX = extras.getInt("outputX");
			mOutputY = extras.getInt("outputY");
			mScale = extras.getBoolean("scale", true);
			mScaleUp = extras.getBoolean("scaleUpIfNeeded", true);
			// 人脸识别
			mDoFaceDetection = extras.containsKey("noFaceDetection") ? !extras
					.getBoolean("noFaceDetection") : false;
		}

		// 创建MediaItem
		if (mBitmap == null) {
			Uri target = intent.getData();
			String targetScheme = target.getScheme();
			int rotation = 0;
			ParcelFileDescriptor fileDescriptor = CropUtils.getPFD(target,
					mContentResolver);
			mBitmap = CropUtils.thumbBitmap(fileDescriptor);
			if (mBitmap==null){
				mBitmap = CropUtils.thumbBitmap(fileDescriptor);
				if (mBitmap==null){
					mBitmap = CropUtils.thumbBitmap(fileDescriptor);
				}
			}
			if (targetScheme.equals("file")) {
				// 查看照片的信息
				ExifInterface exif;
				try {
					exif = new ExifInterface(target.getPath());
					// 查看照片的方向
					rotation = CropUtils.getImageOrientationToDegrees(exif
							.getAttributeInt(ExifInterface.TAG_ORIENTATION,
									ExifInterface.ORIENTATION_NORMAL));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 旋转图片
			if (mBitmap != null && rotation != 0f) {
				mBitmap = CropUtils.rotate(mBitmap, rotation);
			}
		}

		if (mBitmap == null) {
			Toast.makeText(getApplicationContext(), R.string.crop_not_image,
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		findViewById(R.id.discard).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						setResult(RESULT_CANCELED);
						finish();
					}
				});

		findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onSaveClicked();
			}
		});
		startFaceDetection();
	}

	private void startFaceDetection() {
		if (isFinishing()) {
			return;
		}
		mImageView.setImageBitmapResetBase(mBitmap, true);
		BitmapManager.startBackgroundJob(this, null,
				getResources().getString(R.string.crop_running_face_detection),
				new Runnable() {
					public void run() {
						final CountDownLatch latch = new CountDownLatch(1);
						final Bitmap b = mBitmap;
						mHandler.post(new Runnable() {
							public void run() {
								if (b != mBitmap && b != null) {
									mImageView.setImageBitmapResetBase(b, true);
									mBitmap.recycle();
									mBitmap = b;
								}
								if (mImageView.getScale() == 1.0f) {
									mImageView.center(true, true);
								}
								latch.countDown();
							}
						});
						try {
							latch.await();
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
						mRunFaceDetection.run();
					}
				}, mHandler);
	}

	/**
	 * 
	 * 保存,裁剪
	 * 
	 */
	private void onSaveClicked() {
		if (mSaving)
			return;
		if (mCrop == null)
			return;
		mSaving = true;

		Bitmap croppedImage;// 裁剪后的bitmap

		// 裁剪指定的大小,不进行缩放
		if (mOutputX != 0 && mOutputY != 0 && !mScale) {
			croppedImage = Bitmap.createBitmap(mOutputX, mOutputY,
					Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(croppedImage);
			Rect srcRect = mCrop.getCropRect();// 获得裁剪区域的大小
			Rect dstRect = new Rect(0, 0, mOutputX, mOutputY);// 显示填充区域的大小
			int dx = (srcRect.width() - dstRect.width()) / 2;
			int dy = (srcRect.height() - dstRect.height()) / 2;
			// If the srcRect is too big, use the center part of it.
			srcRect.inset(Math.max(0, dx), Math.max(0, dy));
			// If the dstRect is too big, use the center part of it.
			dstRect.inset(Math.max(0, -dx), Math.max(0, -dy));
			// 裁剪bitmap,生成新的bitmap
			canvas.drawBitmap(mBitmap, srcRect, dstRect, null);
			// 释放空间
			mImageView.clear();
			mBitmap.recycle();
		} else {
			// 获得裁剪区域的大小
			Rect r = mCrop.getCropRect();
			int width = r.width();
			int height = r.height();

			// If we are circle cropping, we want alpha channel, which is the
			// third param here.
			croppedImage = Bitmap.createBitmap(width, height,
					mCircleCrop ? Bitmap.Config.ARGB_8888
							: Bitmap.Config.RGB_565);

			Canvas canvas = new Canvas(croppedImage);
			Rect dstRect = new Rect(0, 0, width, height);
			canvas.drawBitmap(mBitmap, r, dstRect, null);

			// Release bitmap memory as soon as possible
			mImageView.clear();
			mBitmap.recycle();

			if (mCircleCrop) {
				// OK, so what's all this about?
				// Bitmaps are inherently rectangular but we want to return
				// something that's basically a circle. So we fill in the
				// area around the circle with alpha. Note the all important
				// PortDuff.Mode.CLEAR.
				Canvas c = new Canvas(croppedImage);
				Path p = new Path();
				p.addCircle(width / 2F, height / 2F, width / 2F,
						Path.Direction.CW);
				c.clipPath(p, Region.Op.DIFFERENCE);
				c.drawColor(0x00000000, PorterDuff.Mode.CLEAR);
			}
			// If the required dimension is specified, scale the image.
			if (mOutputX != 0 && mOutputY != 0 && mScale) {
				croppedImage = CropUtils.transform(new Matrix(), croppedImage,
						mOutputX, mOutputY, mScaleUp, true);
			}
		}
		/*
		 * Rect r = mCrop.getCropRect(); int width = r.width(); // CR: final ==
		 * happy panda! int height = r.height();
		 * 
		 * // If we are circle cropping, we want alpha channel, which is the //
		 * third param here. Bitmap croppedImage = Bitmap.createBitmap(width,
		 * height, mCircleCrop ? Bitmap.Config.ARGB_8888 :
		 * Bitmap.Config.RGB_565); { Canvas canvas = new Canvas(croppedImage);
		 * Rect dstRect = new Rect(0, 0, width, height);
		 * canvas.drawBitmap(mBitmap, r, dstRect, null); }
		 * 
		 * if (mCircleCrop) { // OK, so what's all this about? // Bitmaps are
		 * inherently rectangular but we want to return // something that's
		 * basically a circle. So we fill in the // area around the circle with
		 * alpha. Note the all important // PortDuff.Mode.CLEARes. Canvas c =
		 * new Canvas(croppedImage); Path p = new Path(); p.addCircle(width /
		 * 2F, height / 2F, width / 2F, Path.Direction.CW); c.clipPath(p,
		 * Region.Op.DIFFERENCE); c.drawColor(0x00000000,
		 * PorterDuff.Mode.CLEAR); }
		 * 
		 * // If the output is required to a specific size then scale or fill.
		 * if (mOutputX != 0 && mOutputY != 0) { if (mScale) { // Scale the
		 * image to the required dimensions. Bitmap old = croppedImage;
		 * croppedImage = CropUtils.transform(new Matrix(), croppedImage,
		 * mOutputX, mOutputY, mScaleUp); if (old != croppedImage) {
		 * old.recycle(); } } else {
		 * 
		 * 
		 * Don't scale the image crop it to the size requested. Create an new
		 * image with the cropped image in the center and the extra space
		 * filled.
		 * 
		 * 
		 * // Don't scale the image but instead fill it so it's the // required
		 * dimension Bitmap b = Bitmap.createBitmap(mOutputX, mOutputY,
		 * Bitmap.Config.RGB_565); Canvas canvas = new Canvas(b);
		 * 
		 * Rect srcRect = mCrop.getCropRect(); Rect dstRect = new Rect(0, 0,
		 * mOutputX, mOutputY);
		 * 
		 * int dx = (srcRect.width() - dstRect.width()) / 2; int dy =
		 * (srcRect.height() - dstRect.height()) / 2;
		 * 
		 * // If the srcRect is too big, use the center part of it.
		 * srcRect.inset(Math.max(0, dx), Math.max(0, dy));
		 * 
		 * // If the dstRect is too big, use the center part of it.
		 * dstRect.inset(Math.max(0, -dx), Math.max(0, -dy));
		 * 
		 * // Draw the cropped bitmap in the center. canvas.drawBitmap(mBitmap,
		 * srcRect, dstRect, null);
		 * 
		 * // Set the cropped bitmap as the new bitmap. croppedImage.recycle();
		 * croppedImage = b; } }
		 */

		// Return the cropped image directly or save it to the specified URI.
		Bundle myExtras = getIntent().getExtras();
		if (myExtras != null
				&& (myExtras.getParcelable("data") != null || myExtras
						.getBoolean("return-data"))) {
			Bundle extras = new Bundle();
			extras.putParcelable("data", croppedImage);
			setResult(RESULT_OK, (new Intent()).setAction("inline-data")
					.putExtras(extras));
			finish();
		} else {
			final Bitmap b = croppedImage;
			final Runnable save = new Runnable() {
				public void run() {
					saveOutput(b);
				}
			};
			BitmapManager.startBackgroundJob(this, null, getResources()
					.getString(R.string.crop_saving_image), save, mHandler);
		}
	}

	/**
	 * 保存bitmap到文件
	 * 
	 * @param croppedImage
	 */
	private void saveOutput(Bitmap croppedImage) {
		if (mSaveUri != null) {
			OutputStream outputStream = null;
			try {
				outputStream = mContentResolver.openOutputStream(mSaveUri);
				if (outputStream != null) {
					croppedImage.compress(mOutputFormat, 80, outputStream);
				}
				// TODO ExifInterface write
			} catch (IOException ex) {
				Log.e(TAG, "Cannot open file: " + mSaveUri, ex);
			} finally {
				CropUtils.closeSilently(outputStream);
			}
			Bundle extras = new Bundle();
			extras.putParcelable("data", mSaveUri);
			setResult(RESULT_OK, new Intent().putExtras(extras));
		}
		croppedImage.recycle();
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	Runnable mRunFaceDetection = new Runnable() {
		float mScale = 1F;
		Matrix mImageMatrix;
		FaceDetector.Face[] mFaces = new FaceDetector.Face[3];
		int mNumFaces;

		// For each face, we create a HightlightView for it.
		private void handleFace(FaceDetector.Face f) {
			PointF midPoint = new PointF();

			int r = ((int) (f.eyesDistance() * mScale)) * 2;
			f.getMidPoint(midPoint);
			midPoint.x *= mScale;
			midPoint.y *= mScale;

			int midX = (int) midPoint.x;
			int midY = (int) midPoint.y;

			HighlightView hv = new HighlightView(mImageView);

			int width = mBitmap.getWidth();
			int height = mBitmap.getHeight();

			Rect imageRect = new Rect(0, 0, width, height);

			RectF faceRect = new RectF(midX, midY, midX, midY);
			faceRect.inset(-r, -r);
			if (faceRect.left < 0) {
				faceRect.inset(-faceRect.left, -faceRect.left);
			}

			if (faceRect.top < 0) {
				faceRect.inset(-faceRect.top, -faceRect.top);
			}

			if (faceRect.right > imageRect.right) {
				faceRect.inset(faceRect.right - imageRect.right, faceRect.right
						- imageRect.right);
			}

			if (faceRect.bottom > imageRect.bottom) {
				faceRect.inset(faceRect.bottom - imageRect.bottom,
						faceRect.bottom - imageRect.bottom);
			}

			hv.setup(mImageMatrix, imageRect, faceRect, mCircleCrop,
					mAspectX != 0 && mAspectY != 0);

			mImageView.add(hv);
		}

		// Create a default HightlightView if we found no face in the picture.
		private void makeDefault() {
			HighlightView hv = new HighlightView(mImageView);

			int width = mBitmap.getWidth();
			int height = mBitmap.getHeight();

			Rect imageRect = new Rect(0, 0, width, height);

			// CR: sentences!
			// make the default size about 4/5 of the width or height
			int cropWidth = Math.min(width, height) * 4 / 5;
			int cropHeight = cropWidth;

			if (mAspectX != 0 && mAspectY != 0) {
				if (mAspectX > mAspectY) {
					cropHeight = cropWidth * mAspectY / mAspectX;
				} else {
					cropWidth = cropHeight * mAspectX / mAspectY;
				}
			}

			int x = (width - cropWidth) / 2;
			int y = (height - cropHeight) / 2;

			RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
			hv.setup(mImageMatrix, imageRect, cropRect, mCircleCrop,
					mAspectX != 0 && mAspectY != 0);
			mImageView.add(hv);
		}

		// Scale the image down for faster face detection.
		private Bitmap prepareBitmap() {
			if (mBitmap == null) {
				return null;
			}

			// 256 pixels wide is enough.
			if (mBitmap.getWidth() > 256) {
				mScale = 256.0F / mBitmap.getWidth(); // CR: F => f (or change
														// all f to F).
			}
			Matrix matrix = new Matrix();
			matrix.setScale(mScale, mScale);
			Bitmap faceBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
					mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
			return faceBitmap;
		}

		public void run() {
			mImageMatrix = mImageView.getImageMatrix();
			Bitmap faceBitmap = prepareBitmap();

			mScale = 1.0F / mScale;
			if (faceBitmap != null && mDoFaceDetection) {
				FaceDetector detector = new FaceDetector(faceBitmap.getWidth(),
						faceBitmap.getHeight(), mFaces.length);
				mNumFaces = detector.findFaces(faceBitmap, mFaces);
			}

			if (faceBitmap != null && faceBitmap != mBitmap) {
				faceBitmap.recycle();
			}

			mHandler.post(new Runnable() {
				public void run() {
					mWaitingToPick = mNumFaces > 1;
					if (mNumFaces > 0) {
						for (int i = 0; i < mNumFaces; i++) {
							handleFace(mFaces[i]);
						}
					} else {
						makeDefault();
					}
					mImageView.invalidate();
					if (mImageView.mHighlightViews.size() == 1) {
						mCrop = mImageView.mHighlightViews.get(0);
						mCrop.setFocus(true);
					}

					if (mNumFaces > 1) {
						// CR: no need for the variable t. just do
						// Toast.makeText(...).show().
						Toast t = Toast.makeText(CropImage.this,
								R.string.crop_multiface_crop_help,
								Toast.LENGTH_SHORT);
						t.show();
					}
				}
			});
		}
	};
}


