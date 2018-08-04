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
			
