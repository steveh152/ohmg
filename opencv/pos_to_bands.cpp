#include "cv.h"
#include <iostream>
#include <vector>
#include <cmath>
#include "highgui.h"

using namespace cv;
using namespace std;

std::string get_color(int h, int s, int v){
	if (v<5)return "black";
	if (s<30){
		if (v>75){
			return "white";
		}
		else return "grey";
	}
	if ((h>320||h<=70)&&s<=62)return "brown";
	if (h>320||h<=10)return "red";
	if (h>10&&h<=40)return "orange";
	if (h>40&&h<=70)return "yellow";
	if (h>70&&h<=150)return "green";
	if (h>150&&h<=260)return "blue";
	if (h>260&&h<=320)return "violet";
	return "brown????";
}

int main(int argc, char** argv ) {
	cvNamedWindow("original", CV_WINDOW_AUTOSIZE);
	cvNamedWindow("original2", CV_WINDOW_AUTOSIZE);
	cvNamedWindow("band1",    CV_WINDOW_AUTOSIZE);
	cvNamedWindow("band2",    CV_WINDOW_AUTOSIZE);
	cvNamedWindow("band3",    CV_WINDOW_AUTOSIZE);
	cvNamedWindow("band4",    CV_WINDOW_AUTOSIZE);
	Mat original;

	if (argc != 6 && argc != 7){
		cout<<"usage: "<<argv[0]<<"[image] [band1] [band2] [band3] [band4] [band5]";
	}
	vector<Mat> v;
	original= imread(argv[1], CV_LOAD_IMAGE_COLOR);
	imshow("original", original);
	for (int i = 2; i < argc; ++i)
	{
		cout<<argv[i]<<endl;
		Rect ROI(atoi(argv[i])-5, original.rows/6, 10, original.rows*2/3);
		Mat tmp = original(ROI);
		Mat kernel = getStructuringElement(MORPH_RECT, Size(3,3), Point(1,1));
		erode(tmp, tmp, kernel, Point(1,1), 5);
		Mat hsv;
		cvtColor(tmp, hsv, CV_BGR2HSV);
		vector<Mat> v_channel;
		split(hsv, v_channel);

		int h(0), h1(0), s(0),val(0),count(0);
		for (int x=0;x<hsv.rows;x++){
			for (int y=0; y<hsv.cols;y++){
				h+=v_channel[0].at<uchar>(x,y);
				h1+=(v_channel[0].at<uchar>(x,y)+90)%180;
				s+=v_channel[1].at<uchar>(x,y);
				val+=v_channel[2].at<uchar>(x,y);
				count++;
			}
		}
		h= h/count;
		h1=h1/count;
		s= s/count;
		val= val/count;
		int diff(0), diff1(0);
		for (int x=0;x<hsv.rows;x++){
			for (int y=0; y<hsv.cols;y++){
				diff+=abs(h-v_channel[0].at<uchar>(x,y));
				diff1+=abs(h1-(90+v_channel[0].at<uchar>(x,y))%180);
			}
		}
		if (diff1<diff)h=(h1+90)%180;
		h= h*2;
		s= s*100/255;
		val= val*100/255;
		std::cout<<h<<"  "<<s<<"  "<<val<<endl;
		std::cout<<get_color(h,s,val)<<endl;
		v.push_back(tmp);
	}
	imshow("original2", original);
	imshow("band1", v[0]);
	imshow("band2", v[1]);
	imshow("band3", v[2]);
	imshow("band4", v[3]);
	while(1){
		char c= cvWaitKey(33);
		if (c==27)break;
	}
	return 1;
}