import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class Function {
    ChessPane cl;
    public Function(ChessPane cl){
        this.cl = cl;
    }

    public static JButton createBtn(String text, String icon,int x,int y,int width,int height) {
        JButton btn = new JButton(text, new ImageIcon(icon));
        btn.setUI(new BasicButtonUI());// 恢复基本视觉效果
//        btn.setPreferredSize(new Dimension(90, 30));// 设置按钮大小
        btn.setContentAreaFilled(true);// 设置按钮透明
        btn.setFont(new Font("粗体", Font.PLAIN, 15));// 按钮文本样式
        btn.setForeground(Color.BLACK);
        btn.setMargin(new Insets(0, 0, 0, 0));// 按钮内容与边框距离
        btn.setBounds(x,y,width,height);
        return btn;
    }

    public int countChess(int xChange,int yChange,int color){
        int tempX = xChange;
        int tempY = yChange;
        int x = cl.x;
        int y = cl.y;
        int count = 1;
        while(x+xChange<15&&y+yChange>=0&&y+yChange<15&&cl.allChess[y+yChange][x+xChange]==color){
            count++;
            if(xChange!=0)
                xChange++;
            if(yChange>0)
                yChange++;
            else if(yChange<0)
                yChange--;
        }
        xChange = tempX;
        yChange = tempY;
        while(x-xChange>=0&&y-yChange>=0&&y-yChange<15&&cl.allChess[y-yChange][x-xChange]==color){
            count++;
            if(xChange!=0)
                xChange++;
            if(yChange>0)
                yChange++;
            else if(yChange<0)
                yChange--;
        }
        return count;
    }

}
