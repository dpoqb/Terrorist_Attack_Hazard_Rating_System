//Run.java
import java.awt.*;
import main.GtdGUIChooser;

import javax.swing.*;

//创建系统主类
class Run{
    public static void main(String[] args)
    {   // 创建主界面，并使主窗口显示在屏幕居中
        try {
            //更改全局UI风格
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        GtdGUIChooser frame = new GtdGUIChooser();
        // 获取屏幕尺寸
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();// 获取主界面的窗体尺寸
        // 令主界面窗体居中
        if (frameSize.height > screenSize.height)
            frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width)
            frameSize.width = screenSize.width;
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        //frame.setResizable(false);
        frame.setVisible(true);// 令主界面显示
    }
}