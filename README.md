# 自定义控件Demo：
**CustomControl：ViewGroup实现竖向引导界面**

**1、自定义控件的执行顺序：** onMeasure -> onSizeChanged -> onLayout - > onDraw - computeScroll(被不断的不断调用);

**2、getScrollY（）**

	在最顶端为0，在最末端(底部)为getMeasuredHeight() - mScreenHeight。
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
			








**自定义控件CustomViewGroup**：分别将0，1，2，3位置的childView依次设置到左上、右上、左下、右下的位置


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




**6.妹纸小练习**

https://gank.io/api/data/福利/10/1

    StaggeredGridLayoutManager获取最后一条可见的位置
    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int[] lastPosition = layoutManager.findLastVisibleItemPositions(null);
                    lastVisibleItemPosition = Math.max(lastPosition[0], lastPosition[1]);
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 2 >= layoutManager.getItemCount()) {
                       //Do nothing
                    }
                }
            });
    Gson解析：
    private List<MeiZhi> parse(String jsonData) {
        return new Gson().fromJson(jsonData, new TypeToken<List<MeiZhi>>() {
               }.getType());
    }



    <?xml version="1.0" encoding="utf-8"?>
    <android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context="com.tang.customcontrol.ui.BehaviorZhiHuActivity">

        <android.support.design.widget.AppBarLayout
            android:layout_width="match_parent"
            android:layout_height="?actionBarSize">

            <android.support.v7.widget.Toolbar
                android:id="@+id/toolbar"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                app:layout_scrollFlags="scroll|enterAlways" />
        </android.support.design.widget.AppBarLayout>

        <android.support.v4.widget.SwipeRefreshLayout
            android:id="@+id/refreshLayout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_behavior="@string/appbar_scrolling_view_behavior">

            <android.support.v7.widget.RecyclerView
                android:id="@+id/recyclerView"
                android:layout_width="match_parent"
                android:layout_height="match_parent" />
        </android.support.v4.widget.SwipeRefreshLayout>

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="?actionBarSize"
            android:layout_gravity="bottom"
            android:background="@color/colorPrimary"
            android:gravity="center"
            app:layout_behavior="@string/myBottomBarBehavior">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:text="这是一个底栏"
                android:textColor="#ffffff" />
        </LinearLayout>

    </android.support.design.widget.CoordinatorLayout>

    //底部BottomBar
    public class MyBottomBarBehavior extends CoordinatorLayout.Behavior<View> {

        public MyBottomBarBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
    //        return super.layoutDependsOn(parent, child, dependency);
            return dependency instanceof AppBarLayout;
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
            child.setTranslationY(Math.abs(dependency.getTop()));
    //        return super.onDependentViewChanged(parent, child, dependency);
            return true;
        }
    }

    public class MyFabBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

        public MyFabBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
    //        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
            return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;//判断是否竖直滚动
        }

        @Override
        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dx, int dy, int[] consumed) {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
            //dy大于0是向上滚动 小于0是向下滚动
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
            return super.onDependentViewChanged(parent, child, dependency);
        }
    }
    //仿百度
     behavior = BottomSheetBehavior.from(mIv);
     behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
     behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);//折叠
     behavior.setState(BottomSheetBehavior.STATE_EXPANDED);//展开


**HaoRecyclerView:**
	
	//装饰
	public void setAdapter(Adapter adapter) {
        footerAdapter = new FooterAdapter(this, moreView, adapter);
        super.setAdapter(footerAdapter);
        adapter.registerAdapterDataObserver(observer);
    }
	
	//重新注册观察者
	private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            footerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            footerAdapter.notifyItemChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            footerAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            footerAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            footerAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            footerAdapter.notifyItemRangeRemoved(fromPosition, toPosition);
        }
    };
		
	//滑动监听
	    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE && loadMoreListener != null && !isLoading && isCanLoadMore) {
            int lastVisibleItem;
            LayoutManager manager = getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                lastVisibleItem = ((GridLayoutManager) manager).findLastVisibleItemPosition();
            } else if (manager instanceof StaggeredGridLayoutManager) {
                int[] position = ((StaggeredGridLayoutManager) manager).findFirstVisibleItemPositions(null);
                lastVisibleItem = lastPositions(position);
            } else {
                lastVisibleItem = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
            }

            if (manager.getChildCount() > 0 && lastVisibleItem >= manager.getChildCount() - 1) {
                isLoading = true;
                loadMoreListener.onLoadMore();
            }
        }
    }

    private int lastPositions(int[] positions) {
        int last = positions[0];
        for (int position : positions) {
            if (position > last)
                last = position;
        }
        return last;
    }

	//重写FooterAdapter基本方法
	 /**
     * 解决FootView在不同的Manager下独占一行
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final int spanCount = ((GridLayoutManager) manager).getSpanCount();
            ((GridLayoutManager) manager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == FOOTVIEW)
                        return spanCount;
                    return 1;
                }
            });
        }
    }
    /**
     * 解决流水布局，保证加载横多独占屏幕宽度
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams
                && isFooter(holder.getLayoutPosition())) {
            StaggeredGridLayoutManager.LayoutParams lp =
                    (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            lp.setFullSpan(true);
        }
    }

**LayoutAnimation动画**
	
	1、首先定义item动画：
	<?xml version="1.0" encoding="utf-8"?>
	<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="250">

    <translate
        android:fromYDelta="-20%"
        android:interpolator="@android:anim/decelerate_interpolator"
        android:toYDelta="0" />

    <alpha
        android:fromAlpha="0"
        android:interpolator="@android:anim/decelerate_interpolator"
        android:toAlpha="1" />

    <scale
        android:fromXScale="105%"
        android:fromYScale="105%"
        android:interpolator="@android:anim/decelerate_interpolator"
        android:pivotX="50%"
        android:pivotY="50%"
        android:toXScale="100%"
        android:toYScale="100%" />

	</set>
	
	2、定义LayoutAnimation动画：
	<?xml version="1.0" encoding="utf-8"?>
	<layoutAnimation xmlns:android="http://schemas.android.com/apk/res/android"
    android:animation="@anim/item_animation_from_bottom"
    android:animationOrder="normal"
    android:delay="15%" />

	3、引用
	xml方式：
	android:layoutAnimation="@anim/layout_animation_fall_down"
	代码方式：
	LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
	viewGroup.setLayoutAnimation(controller);
	//数据发生变化是调用
	viewGroup.scheduleLayoutAnimation();


**ShadowLayout阴影**
	
步骤1：

	 /**
     *  阴影的大小范围
     */
     private float radius;
     /**
     *  阴影x轴的偏移量
     */
     private float dx;
     /**
     *  阴影y轴的偏移量
     */
     private float dy;
     /**
     *  阴影颜色
     */
     private int shadowColor;

	 public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);//保证onDraw()执行
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关闭硬件加速

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        if (typedArray != null) {
            radius = typedArray.getDimension(R.styleable.ShadowLayout_ShadowLayout_Radius, dip2px(5));
            dx = typedArray.getDimension(R.styleable.ShadowLayout_ShadowLayout_Dx, dip2px(0));
            dy = typedArray.getDimension(R.styleable.ShadowLayout_ShadowLayout_Dy, dip2px(0));
            shadowColor = typedArray.getColor(R.styleable.ShadowLayout_ShadowLayout_ShadowColor,
                    ContextCompat.getColor(getContext(), android.R.color.black));
            mShadowSide = typedArray.getInt(R.styleable.ShadowLayout_ShadowLayout_Side, ALL);
            typedArray.recycle();
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//mPaint.setAntiAlias(true)
        mPaint.setColor(Color.TRANSPARENT);//透明
        mPaint.setShadowLayer(radius, dx, dy, shadowColor);
    }
	
步骤2：

	//onMeasure、onLayout改变ViewGroup的高宽预留出阴影的面积
	
步骤3：

	//实现阴影
	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(mRectF, mPaint);
    }