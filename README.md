# 自定义控件Demo：
CustomControl：ViewGroup实现竖向引导界面

1、自定义控件的执行顺序：onMeasure -> onSizeChanged -> onLayout - > onDraw - computeScroll(被不断的不断调用);

2、getScrollY（），在最顶端为0，在最末端(底部)为getMeasuredHeight() - mScreenHeight。
疑问：为什么在做临界值判断时不用：if (dy < 0 && scrollY <= 0)//往下拉，而是 if (dy < 0 && scrollY + offsetY <= 0)//往下拉,
		 	offsetY是当前的移动的Y与上次滑动的Y的差值，
      
      错误代码段：
                if (dy < 0 && scrollY + dy <= 0)//往下拉,往下拉多少,
                {
                    return true;
                } else if (dy > 0 && scrollY + dy > getMeasuredHeight() - mScreenHeight) {
                    return true;
                } else {
                    scrollBy(0, dy);
                    mLastY = y;
                }
      正确代码段：
      		//(偏移量是这次相对上次跑了多少，偏移的坐标与手指触动的方向相反，所以dy采用上次的坐标y-这次的y坐标的差值)
                int dy = mLastY - y;
                //边界值检查
                int scrollY = getScrollY();//当前的偏移量
                Log.e(TAG, "ACTION_MOVE：scrollY ---- " + scrollY + "-------dy:" + dy);

                if (dy < 0 && scrollY + dy <= 0)//往下拉,到达顶端，(scrollY + dy)总的需要偏移的距离
                {
                    dy = -scrollY;//在达到顶端的时候偏移多少就就反方向移动多少，以保持维持在总偏移量在0处
                } else if (dy > 0 && scrollY + dy > getMeasuredHeight() - mScreenHeight) {//往上滑动，到达底端
                    dy = getMeasuredHeight() - mScreenHeight - scrollY;
                    //在达到底部的时候偏移多少就就反方向移动多少，以保持维持在总偏移量在底部处
                }
                scrollBy(0, dy);
                mLastY = y;
		break;
			








自定义控件CustomViewGroup：分别将0，1，2，3位置的childView依次设置到左上、右上、左下、右下的位置

**1. viewGroup的职责：**

    给childView计算出建议的宽高和测量模式；决定childView的位置，为什么只是建议的宽高，而不是确定，是因为childView的
	宽高可以设置为wrap_content,

**2. view的职责**

	根据测量模式和ViewGoup给出的建议的宽高，计算出自己的宽高，同时还有个职责就是在viewGroup制定的区域内绘制自己的形态。

**3. ViewGroup和LayoutParams的关系：**
	
	每一个ViewGroup都需要指定一个LayoutParams,用于确定childView支持那些属性，比如LinearLayout.LayoutParams,
	Relayout.LayoutParams，我们自定义控件的时候设置的LayoutParams一般直接使用系统的MarginLayoutParams，
	MarginLayoutParams只支持margin属性。generateLayoutParams()实现设置LayoutParams

**4. View的三种测量模式：**

	EXACTLY：表示设置了精确的值，一般当childView设置宽高为精确值、match_parent时，viewGroup会将其设置为该属性
    AT_MOST：表示子布局被限制在一个最大值内，一般当宽高设置为wrap_content时，viewGroup会将其设置为MOST
    UNSPECIFIED：表示子布局想要多大就多大,一把出现在AdapterView的item的heightMode中、ScrollView的childView的heightMode中，
    上述三个均不是绝对的，对于childVIew的mode的设置还会与viewGroup的测量模式有一定的关系，深入内容暂时不深究

**5. 从APi角度解析**
	
	在onMeasure()中，View和ViewGroup根据本身的父控件传入的测量值和模式，对自己的宽高进行确定，在onLayout(）
	中完成childView的位置的指定，直到本身时View时，然后在onDrow()中完成自己的形态绘制。
