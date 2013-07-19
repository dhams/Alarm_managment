package com.medplan.app;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.mail.internet.NewsAddress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.medpan.util.Constant;
import com.medpan.util.GlobalMethods;
import com.medpan.util.Notification_Model;
import com.medpan.util.Picture_Model;
import com.medpan.util.Report_Model;
import com.medpan.util.User_Model;
import com.medplan.app.BoxThreeActivity.AlertDialgo;
import com.medplan.db.databasehelper;

public class UserReportActivity extends Activity {
	databasehelper db;
	int userid = 0;
	TextView headerTitle, tvNoreport;
	ImageView headLogo;
	LinearLayout mainLayout;
	RelativeLayout HeaderLayout;
	ListView listview;
	Button btnDel , btnDelAll, btnPdf, btnHome;
	public static ArrayList<Report_Model> reportList;
	public static EfficientAdapter adapter;
	public static ArrayList<Boolean> flags = new ArrayList<Boolean>();

	// /PDF
	String FILE1 = Environment.getExternalStorageDirectory() + File.separator
			+ "Medplann";
	Image image;
	Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	Font catFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
	Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
	// Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
	Paragraph paragraph;
	 AlertDialog alertDialog = null  ;
	
	@Override
	public void onCreate(Bundle icicle) {
		overridePendingTransition(0, 0);
		super.onCreate(icicle);

		setContentView(R.layout.users_report);

		Log.i("TAG", "-----User activity-----");

		btnHome = (Button) findViewById(R.id.button_home);
		btnHome.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(UserReportActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
		});
		btnDel = (Button) findViewById(R.id.btnDel);
		btnDelAll = (Button) findViewById(R.id.btnDelAll);
		btnDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int i = 0; i < flags.size(); i++) {
					if (flags.get(i)) {
						db.deleteReport(reportList.get(i).report_id);

					}
				}
				flags.clear();
				reportList = db.getReportInfo(userid);

				if (reportList.size() == 0) {
					tvNoreport.setVisibility(View.VISIBLE);
					btnDel.setVisibility(View.GONE);
					btnPdf.setVisibility(View.GONE);
					listview.setVisibility(View.GONE);
					btnDelAll.setVisibility(View.GONE) ;
				} else {
					btnDelAll.setVisibility(View.VISIBLE) ;
					tvNoreport.setVisibility(View.GONE);
					btnDel.setVisibility(View.VISIBLE);
					btnPdf.setVisibility(View.VISIBLE);
					listview.setVisibility(View.VISIBLE);
					for (int i = 0; i < reportList.size(); i++) {
						flags.add(false);
					}
					adapter = new EfficientAdapter(UserReportActivity.this,
							reportList);
					listview.setAdapter(adapter);
				}

			}
		});

		
 
		AlertDialog.Builder builder = new AlertDialog.Builder(this) ;
		builder.setTitle("Confirm");
		builder.setMessage("Delete all ?");
		
		builder.setPositiveButton(R.string.ok, new  DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				tvNoreport.setVisibility(View.VISIBLE);
				listview.setVisibility(View.GONE);
				btnDel.setVisibility(View.GONE);
				btnDelAll.setVisibility(View.GONE) ;
				btnPdf.setVisibility(View.GONE);
				
					db.deleteAllReport() ;
					reportList.clear() ;
					adapter.notifyDataSetChanged() ;
					
//					adapter = new EfficientAdapter(getApplicationContext(), reportList);
//					listview.setAdapter(adapter) ;
					
		            alertDialog.dismiss() ; 			
			}
		}) ;
		
		builder.setNegativeButton(R.string.no_thanks, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			alertDialog.dismiss();	
			}
			
		});
		
		btnDelAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
		
				alertDialog.show() ;
			
			}
		});
			
		alertDialog = builder.create() ;
		
		btnPdf = (Button)findViewById(R.id.btnPdf);
		btnPdf.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String parentdir;
				parentdir =  Environment.getExternalStorageDirectory() + File.separator
				+ "Medplann";
				File parentDirFile = new File(parentdir);
				parentDirFile.mkdirs();

				// If we can't write to that special path, try just writing
				// directly to the sdcard
				if (!parentDirFile.isDirectory()) {
				parentdir = "/mnt/sdcard";
				}
				
				Document document = new Document();
				File F_Root = new File(parentdir);
				F_Root.mkdir();
				Calendar cal = Calendar.getInstance();
				final File F_PDF = new File(F_Root, "Report"+cal.getTimeInMillis()
						+ ".pdf");

				try {
					PdfWriter.getInstance(document, new FileOutputStream(
							F_PDF));
				} catch (Exception e) {
					e.printStackTrace();
					Toast
							.makeText(
									UserReportActivity.this,
									"You might not have inserted SD Card properly.",
									Toast.LENGTH_LONG).show();
				}
				document.open();
				try {
					addTitlePage(document);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				document.close();
				Toast.makeText(UserReportActivity.this, "Report saved in Medplann folder.", Toast.LENGTH_LONG).show();
			}
		});
		db = new databasehelper(this);

		userid = PreferenceManager.getDefaultSharedPreferences(this).getInt(
				"UserID", 0);

		headerTitle = (TextView) findViewById(R.id.tv_header_title);
		headLogo = (ImageView) findViewById(R.id.img_header_logo);
		headLogo.setBackgroundResource(R.drawable.report_mgt);

		Typeface face = Typeface.createFromAsset(getAssets(),
				"century_gothic.ttf");
		headerTitle.setText(R.string.reprot_mgt);
		// headerTitle.setText(Html.fromHtml("Users" + "</b>" + "<br/>"+
		// "Management"));
		headerTitle.setTypeface(face);

		HeaderLayout = (RelativeLayout) findViewById(R.id.rlHeadTitlelayout);
		mainLayout = (LinearLayout) findViewById(R.id.Mainlinearlayout);

		listview = (ListView) findViewById(R.id.list_user_report);
		tvNoreport = (TextView) findViewById(R.id.tv_noreport);

		try {
			SplashScreen.Cmethod.CheckAddShowScreen(Constant._StrCheck,
					HeaderLayout, mainLayout);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		reportList = db.getReportInfo(userid);

		if (reportList.size() == 0) {
			tvNoreport.setVisibility(View.VISIBLE);
			listview.setVisibility(View.GONE);
			btnDel.setVisibility(View.GONE);
			btnDelAll.setVisibility(View.GONE) ;
			btnPdf.setVisibility(View.GONE);
		} else {
			tvNoreport.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
			btnDel.setVisibility(View.VISIBLE);
			btnDelAll.setVisibility(View.VISIBLE) ;
			btnPdf.setVisibility(View.VISIBLE);
			for (int i = 0; i < reportList.size(); i++) {
				flags.add(false);
			}
			Toast
					.makeText(
							this,
							"Select row and click on delete to remove the report from list.",
							Toast.LENGTH_LONG).show();
			adapter = new EfficientAdapter(getApplicationContext(), reportList);
			listview.setAdapter(adapter);

			
		}
		
	}

	private class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<Report_Model> userReportList;

		public EfficientAdapter(Context context, ArrayList<Report_Model> report) {
			mInflater = LayoutInflater.from(context);
			this.userReportList = report;
		}

		public int getCount() {
			return userReportList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			return position;
		}

		@Override
		public int getViewTypeCount() {
			return userReportList.size();
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater
						.inflate(R.layout.users_report_row, null);
				holder = new ViewHolder();
				holder.patientName = (TextView) convertView
						.findViewById(R.id.tv_pateintName);
				holder.medName = (TextView) convertView
						.findViewById(R.id.tv_MedName);
				holder.Date = (TextView) convertView.findViewById(R.id.tv_Date);
				holder.Time = (TextView) convertView.findViewById(R.id.tv_Time);
				holder.dosageTaken = (TextView) convertView
						.findViewById(R.id.tv_DosTaken);
				holder.cb = (CheckBox) convertView.findViewById(R.id.checkBox1);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.patientName
					.setText(userReportList.get(position).report_patient_name);
			holder.medName
					.setText(userReportList.get(position).report_med_name);
			holder.Date.setText(userReportList.get(position).report_date);
			holder.Time.setText(userReportList.get(position).report_time);
			holder.dosageTaken
					.setText(userReportList.get(position).report_dos_taken);
			holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						flags.set(position, true);
					} else {
						flags.set(position, false);
					}
				}
			});
			return convertView;

		}

		class ViewHolder {

			TextView patientName, medName, Date, Time, dosageTaken;
			CheckBox cb;

		}
	}
	
	private void addTitlePage(Document document) throws DocumentException {

		Paragraph preface = new Paragraph();
		preface.add(new Paragraph("Medplann Alarm Report", catFont));
		preface.setAlignment(Element.ALIGN_CENTER);

		// addEmptyLine(preface, 2);
		document.add(preface);

		Paragraph p = new Paragraph();
		p
				.add(new Paragraph("Date" + "  " + new Date().toGMTString(),
						smallBold));
		p.setAlignment(Element.ALIGN_JUSTIFIED);

		addEmptyLine(p, 1);
		document.add(p);

		Paragraph subCatPart = new Paragraph();
		// Add a table Main
		createTableRC(subCatPart);
		document.add(subCatPart);
	}
	
	private void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	private void createTableRC(Paragraph subCatPart) throws

	BadElementException {
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);
		PdfPCell c1 = new PdfPCell();
		c1.setPhrase(new Paragraph("Patient Name", catFont1));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(BaseColor.GRAY);
		table.addCell(c1);

		c1 = new PdfPCell();
		c1.setPhrase(new Paragraph("Medicine Name", catFont1));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(BaseColor.GRAY);
		table.addCell(c1);

		c1 = new PdfPCell();
		c1.setPhrase(new Paragraph("Date", catFont1));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		// c1.setColspan(2);
		c1.setBackgroundColor(BaseColor.GRAY);
		table.addCell(c1);

		c1 = new PdfPCell();
		c1.setPhrase(new Paragraph("Time", catFont1));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(BaseColor.GRAY);
		table.addCell(c1);

		c1 = new PdfPCell();
		c1.setPhrase(new Paragraph("Quantity", catFont1));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(BaseColor.GRAY);
		table.addCell(c1);
		
		c1 = new PdfPCell();
		c1.setPhrase(new Paragraph("Dosage Taken", catFont1));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(BaseColor.GRAY);
		table.addCell(c1);

		table.setHeaderRows(1);

		for (int i = 0; i < reportList.size(); i++) {
			Report_Model tmp = reportList.get(i);
			PdfPCell c2 = new PdfPCell();
			c2.setPhrase(new Paragraph(tmp.report_patient_name, smallBold));
			c2.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(c2);

			c2 = new PdfPCell();
			c2.setPhrase(new Paragraph(tmp.report_med_name, smallBold));
			c2.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(c2);

			c2 = new PdfPCell();
			c2.setPhrase(new Paragraph(tmp.report_date, smallBold));
			c2.setHorizontalAlignment(Element.ALIGN_LEFT);
			// c2.setColspan(2);
			table.addCell(c2);

			c2 = new PdfPCell();
			c2.setPhrase(new Paragraph(tmp.report_time, smallBold));
			c2.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(c2);
			
			c2 = new PdfPCell();
			c2.setPhrase(new Paragraph((tmp.report_qty/2)+"", smallBold));
			c2.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(c2);

			c2 = new PdfPCell();
			c2.setPhrase(new Paragraph(tmp.report_dos_taken, smallBold));
			c2.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(c2);
		}

		subCatPart.add(table);

	}

}