package cn.ffcs.itbg.itpd.widgets.utils;

import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

/**
 * @Desc: View常用到的工具方法集合
 * @Author: Tyras on 2017/3/21 09:35.
 */

public class ViewUtils {

    /**
     * 测量View的尺寸<br/>
     * 由于Android你中View需要测量后才能得到具体的尺寸数据<br/>
     * 但是我们在使用中，通常都是初始化就需要得到尺寸数据<br/>
     * 因此提供该工具方法，只要对需要尺寸数据的View执行该方法<br/>
     * 然后该View就可以取到尺寸信息。
     *
     * @param child
     */
    public static void measureView(View child) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        // View的宽度信息
        int childMeasureWidth = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        int childMeasureHeight;
        if (lp.height > 0) {
            // 最后一个参数表示：适合、匹配
            childMeasureHeight = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
        } else {
            childMeasureHeight = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED); // 未指定
        }
        //
        child.measure(childMeasureWidth, childMeasureHeight);
    }
}
