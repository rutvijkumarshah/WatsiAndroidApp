package codepath.watsiapp.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import codepath.watsiapp.R;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class PatientListFragment extends Fragment {

	private ProgressBar progressBarToday;
	private String url="https://d3w52z135jkm97.cloudfront.net/uploads/profile/image/2237/profile_Ny-33537.pre_op-03_.jpg";
			//progressBarImageView
	private ImageView img;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_patient_list, container,
				false);

		progressBarToday=(ProgressBar)v.findViewById(R.id.progressBarToday);
		
		img=(ImageView)v.findViewById(R.id.progressBarImageView);
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		
		imageLoader.displayImage(url, img);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setupIintialViews();
	}

	private void setupIintialViews() {

	}

	public void loadUsers() {
	}

	public static PatientListFragment newInstance() {
		PatientListFragment fragment = new PatientListFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
	
	public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
		Bitmap result=null;
		Canvas canvas=null;
		try {
		result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
		Bitmap.Config.ARGB_8888);
		canvas = new Canvas(result);

		int color = 0xff424242;
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF rectF = new RectF(rect);
		int roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		} catch (NullPointerException e) {
		// return bitmap;
		} catch (OutOfMemoryError o){}
		return result;
		
		}
}
