import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HomePane extends JPanel { //主页面板
    MainFrame mainFrame;
    BufferedImage bgImg = null;

    //    按钮控件
    JButton restart = Function.createBtn("开始游戏", null, 235, 130, 140, 40);
    JButton ins = Function.createBtn("游戏说明", null, 235, 220, 140, 40);
    JButton about = Function.createBtn("关于", null, 235, 310, 140, 40);
    JButton exit = Function.createBtn("退出", null, 235, 400, 140, 40);


    public HomePane(MainFrame m) {
        mainFrame = m;

        try {
            bgImg = ImageIO.read(new File("src\\images\\background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(null);

        UIManager.put("Button.font",new Font("微软雅黑",0,12));
        UIManager.put("Label.font",new Font("微软雅黑",0,12));

        this.add(restart);
        this.add(ins);
        this.add(about);
        this.add(exit);
//        开始游戏按钮
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChessPane ch = new ChessPane(mainFrame);
                setVisible(false);
                setEnabled(false);
                mainFrame.add(ch);
                ch.setVisible(true);
                ch.setEnabled(true);
            }
        });
//        游戏说明按钮
        ins.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "游戏说明：\n"
                                + "进入游戏界面可进行游戏时间的设置\n（0表示无时间限制，只能输入正数或0）\n" +
                        "在未连成五子的情况下，游戏时间先耗尽的一方输\n"
                        + "游戏时间内，先连成五子的一方赢", "游戏说明",
                        JOptionPane.PLAIN_MESSAGE);
            }
        });
//        关于按钮
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "游戏制作人：ZhongSir\n"
                        + "制作时间：2019.1.9\n" + "版本号：1.0", "关于",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
//        退出游戏按钮
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        this.repaint();
        setOpaque(false);
        setVisible(true);
    }

    public void paintComponent(Graphics g) {
        BufferedImage bi = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB);
        Graphics g1 = bi.createGraphics();

        g1.drawImage(bgImg, 0, 0, this);
        g1.setColor(Color.BLACK);
        g1.setFont(new Font("华文行楷", Font.BOLD, 50));
        g1.drawString("欢乐五子棋", 170, 70);
        g1.setColor(new Color(0, 0, 0, (float) 0.5));
        g1.fillRect(180, 100, 250, 400);

        g.drawImage(bi, 0, 0, 600, 600, this);
    }
}