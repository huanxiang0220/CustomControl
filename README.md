# 自定义控件Demo：
CustomControl：ViewGroup实现竖向引导界面

1：getScrollY（），在最顶端为0，在最末端(底部)为getMeasuredHeight() - mScreenHeight。
疑问：为什么在做临界值判断时不用：if (dy < 0 && scrollY <= 0)//往下拉，而是 if (dy < 0 && scrollY + offsetY <= 0)//往下拉,
		 	offsetY是当前的移动的Y与上次滑动的Y的差值，当你慢慢滑动的时候取得的scrollY的值等于滑动的Y的差值(不是offset),但是快速拖动
			MotionEvent.ACTION_MOVE的是时候scrollY取得结果要比实手指实际滑动的距离多，但结束动画滑动的时候再次取得的结果是一致的，
      
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
                当快速滑动的时候，一样会因为系统动画的原因导致的差值提前跳出，停止，会导致显示不完整，
      正确代码段：
               case MotionEvent.ACTION_MOVE:
                int dy = mLastY - y;
                //边界值检查
                int scrollY = getScrollY();//可以理解为实际滑动了的距离（慢速滑动的时候等于手指之际滑动的距离）
                Log.e(TAG, "ACTION_MOVE：scrollY ---- " + scrollY + "-------dy:" + dy);

                if (dy < 0 && scrollY + dy <= 0)//往下拉,到达顶端，
                {
                    dy = -scrollY;//快速滑动的时候实际滑动了多少，就反方向移动多少
                } else if (dy > 0 && scrollY + dy > getMeasuredHeight() - mScreenHeight) {//往上滑动，到达底端
                    dy = getMeasuredHeight() - mScreenHeight - scrollY;//快速滑动的时候实际滑动了多少，就反方向移动多少
                }
                scrollBy(0, dy);
                mLastY = y;
                break;
			
