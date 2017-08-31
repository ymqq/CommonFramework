package cn.ffcs.itbg.itpd.tools;

/**
 *  用来生成适配各种屏幕分辨率
 *        基于设计图尺寸，生成需要适配的各种屏幕分辨率下的px换算值，然后直接在布局以及代码中使用对应的w或h的dimen值即可。
 *
 *        在适配过程中遇到的问题记录：
 *        1、适配5" 1920x1080设备时，系统自动适配的方案是1280x800，这样就会出现UI尺寸比设计的小很多，通过获取系统适配出的方案数据，可以得到
 *          该情况下的density为3，这里就在1280x800的方案中增加xxhdpi-1280x800使用的demin来自1920x1080方案即可。
 *          同样的，其他分辨率要是适配有问题的时候都应该使用该方案定位一下，具体被适配为哪个方案了，然后将设备真实的分辨率方案往目标方案上增加一个方案。
 *          -------------------------------------------------------------------------------
            // 这里查看下到底是获取哪个分辨率下的资源，h1334、w750为适配方案中的最大分辨率名称。
            float h1334 = getResources().getDimensionPixelSize(R.dimen.h1334);
            float w750 = getResources().getDimensionPixelSize(R.dimen.w750);
            float density = getResources().getDisplayMetrics().density;
            float densityDpi = getResources().getDisplayMetrics().densityDpi;
            Log.d("DM", "<w, h>----<" + w750 + ", " + h1334 + ">");
            Log.d("DM", "<density, densityDpi>----<" + density + ", " + densityDpi + ">");
 *          -------------------------------------------------------------------------------
 * @author ymqq on 2015/12/19 21:08.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class MakeDeminsXmlTool {

    private static String rootPath = "";

    // 设计图使用的图纸尺寸，该尺寸作为基础尺寸，即可以直接使用设计图上的所有标注
    // 具体适配时，需要修改该值
    private final static int baseW = 750;
    private final static int baseH = 1334;

    // 这里与上面baseW和baseH的值相同，只是为float类型，用于计算转换倍数时精确到小数点，使精确度更高。
    private final static float dw = 750f;
    private final static float dh = 1334f;

    private final static String WTemplate = "    <dimen name=\"w{0}\">{1}px</dimen>\n";
    private final static String HTemplate = "    <dimen name=\"h{0}\">{1}px</dimen>\n";

    static {
        rootPath = new File("").getAbsolutePath() + File.separator + "DimensValues" + File.separator + "values-{0}x{1}" + File.separator;
    }

    public static void main(String[] args) {
        makeString(320, 480);
        makeString(480, 800);
        makeString(480, 854);
        makeString(540, 960);
        makeString(600, 1024);
        makeString(720, 1184);
        makeString(720, 1196);
        makeString(720, 1280);
        makeString(768, 1024);
        makeString(800, 1280);
        makeString(1080, 1812);
        makeString(1080, 1920);
        makeString(1440, 2560);

        System.out.println(rootPath);
    }

    public static void makeString(int w, int h) {

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sb.append("<resources>\n");
        float cellw = w / dw;
        for (int i = 1; i <= baseW; i++) {
            sb.append(WTemplate.replace("{0}", i + "").replace("{1}", change(cellw * i) + ""));
        }
        sb.append("</resources>");

        StringBuilder sb2 = new StringBuilder();
        sb2.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sb2.append("<resources>\n");
        float cellh = h / dh;
        for (int i = 1; i <= baseH; i++) {
            sb2.append(HTemplate.replace("{0}", i + "").replace("{1}", change(cellh * i) + ""));
        }
        sb2.append("</resources>");

        String path = rootPath.replace("{0}", h + "").replace("{1}", w + "");
        File rootFile = new File(path);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
        File layxFile = new File(path + "lay_w.xml");
        File layyFile = new File(path + "lay_h.xml");
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(layxFile));
            pw.print(sb.toString());
            pw.close();
            pw = new PrintWriter(new FileOutputStream(layyFile));
            pw.print(sb2.toString());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static float change(float a) {
        int temp = (int) (a * 100);
        return temp / 100f;
    }
}
