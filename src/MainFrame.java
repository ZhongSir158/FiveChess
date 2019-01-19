import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    //获得显示屏宽高
    int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    int height = Toolkit.getDefaultToolkit().getScreenSize().height;

//    ChessPane chPane = new ChessPane(this);
    HomePane hmPane = new HomePane(this);


    public MainFrame(){
        this.add(hmPane);

        this.setTitle("五子棋");
        this.setSize(600, 600);
        this.setLocation((width - 600) / 2, (height - 600) / 2); //将窗口置于屏幕中间
        this.setResizable(false); //禁止改变窗口大小

//        将窗口的关闭方式设置为默认关闭后程序结束
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.setVisible(true);
    }

    public static void main(String[] args){
        new MainFrame();
    }
}
