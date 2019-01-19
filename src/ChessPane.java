import javafx.util.Pair;

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
import java.util.Stack;

public class ChessPane extends JPanel implements MouseListener, Runnable { //下棋面板
    //获得鼠标点击坐标
    int x, y;
    //    棋盘棋子的情况
    int[][] allChess = new int[15][15];
    //    背景图片
    BufferedImage bgImg = null;
    BufferedImage chImg = null;
    BufferedImage infImg = null;
    //    棋子图片
    BufferedImage whImg = null;
    BufferedImage bkImg = null;
    //    黑子下棋
    boolean isBlack = true;
    //    可以继续下棋
    boolean isOk = true;
    //    第一次设置游戏时间
    boolean first = true;

    //    游戏提示信息
    String message = "黑方先行";
    //    游戏时间
    float maxTime = 0;
    int bTime, wTime;
    //按键
    JButton home = Function.createBtn("主页面", null, 480, 100, 90, 30);
    JButton restart = Function.createBtn("重新开启", null, 480, 160, 90, 30);
    JButton res = Function.createBtn("悔棋", null, 480, 240, 90, 30);
    JButton bow = Function.createBtn("认输", null, 480, 300, 90, 30);
    JButton set = Function.createBtn("游戏设置", null, 480, 430, 90, 30);
    JButton exit = Function.createBtn("退出游戏", null, 480, 490, 90, 30);

    //保存下棋的记录
    Stack position = new Stack();

    MainFrame mainFrame;

    //    倒计时线程
    Thread t = new Thread(this);
    Function f;

    public ChessPane(MainFrame m) {
        mainFrame = m;
        //        设置背景图
        try {
            whImg = ImageIO.read(new File("src\\images\\whiteChessman.png"));
            bkImg = ImageIO.read(new File("src\\images\\blackChessman.png"));
            bgImg = ImageIO.read(new File("src\\images\\background.jpg"));
            chImg = ImageIO.read(new File("src\\images\\chessBoard.jpg"));
            infImg = ImageIO.read(new File("src\\images\\inform.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        f = new Function(this); //声明一个功能函数对象，方便调用其方法

        setLayout(null);

        this.add(home);
        this.add(restart);
        this.add(res);
        this.add(bow);
        this.add(set);
        this.add(exit);
//        返回主页
        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null,
                        "再次返回游戏，得重新开始", "inform", JOptionPane.YES_NO_OPTION);
                if (result == 0) {
                    HomePane hm = new HomePane(mainFrame);
                    setVisible(false);
                    setEnabled(false);
                    hm.setVisible(true);
                    hm.setEnabled(true);
                    mainFrame.add(hm);
                }
            }
        });
//        重新开始游戏
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartActionPerformed();
            }
        });
//        悔棋
        res.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resActionPerformed();
            }
        });
//        认输
        bow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bowActionPerformed(e);
            }
        });
//        时间设置
        set.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setActionPerformed(e);
            }
        });
//        退出游戏
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitActionPerfomed();
            }
        });

        //增加鼠标监听器
        addMouseListener(this);
        this.repaint();
        setOpaque(false);
    }

    public void paintComponent(Graphics g) {
        BufferedImage bi = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB);
        Graphics g1 = bi.createGraphics();

        g1.drawImage(bgImg, 0, 0, this); //设置背景图片
        g1.drawImage(chImg, 40, 100, 420, 420, this); //设置棋盘背景
        g1.drawImage(infImg, 100, 0, 343, 120, this);
        g1.setColor(new Color(123, 76, 22));  //设置字体、线条颜色
//        绘制棋盘
        for (int i = 40; i <= 460; i = i + 30) {
            g1.drawLine(40, i + 60, 460, i + 60);
            g1.drawLine(i, 100, i, 520);
        }
//        绘制棋盘五点
        g1.fillOval(127, 187, 6, 6);
        g1.fillOval(367, 187, 6, 6);
        g1.fillOval(367, 427, 6, 6);
        g1.fillOval(127, 427, 6, 6);
        g1.fillOval(247, 307, 6, 6);
//        游戏信息
        g1.setColor(Color.white); //字体颜色
        g1.setFont(new Font("宋体", Font.BOLD, 25)); //字体设置
        g1.drawString("游戏信息：", 172, 70);
        g1.setFont(new Font("华文行楷", Font.BOLD, 25));
        g1.drawString(message, 287, 68);

//        时间
        g1.setColor(Color.BLACK);
        g1.setFont(new Font("华文行楷", Font.PLAIN, 22));
        g1.drawRect(40, 530, 200, 30);
        g1.drawRect(260, 530, 200, 30);

        g1.drawString("白方时间:", 45, 553);
        if (maxTime == 0) {
            g1.drawString("无限制", 162, 553);
        } else
            g1.drawString(((wTime / 3600 >= 10) ? wTime / 3600 : ("0" + (wTime / 3600)))
                    + ":" + ((wTime / 60 % 60 >= 10) ? (wTime / 60 % 60) : ("0" + (wTime / 60 % 60)))
                    + ":" + ((wTime % 60 >= 10) ? (wTime % 60) : ("0" + wTime % 60)), 148, 553);
        g1.drawString("黑方时间:", 265, 553);
        if (maxTime == 0) {
            g1.drawString("无限制", 382, 553);
        } else
            g1.drawString(((bTime / 3600 >= 10) ? bTime / 3600 : ("0" + (bTime / 3600)))
                    + ":" + ((bTime / 60 % 60 >= 10) ? (bTime / 60 % 60) : ("0" + (bTime / 60 % 60)))
                    + ":" + ((bTime % 60 >= 10) ? (bTime % 60) : ("0" + bTime % 60)), 368, 553);

//        棋子
        for (int i = 0; i < 15; i++)
            for (int j = 0; j < 15; j++) {
                int tempX = (j + 1) * 30;
                int tempY = (i + 3) * 30;

                if (allChess[i][j] == 1) { //黑棋
                    g1.drawImage(bkImg, tempX + 2, tempY + 2, 16, 16, this);
                } else if (allChess[i][j] == -1) { //白棋
                    g1.drawImage(whImg, tempX + 2, tempY + 2, 16, 16, this);
                }
            }
        g.drawImage(bi, 0, 0, this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isOk) {
            //        得到当前鼠标点击的坐标
            x = (int) e.getX();
            y = (int) e.getY();

            if (x >= 40 && x <= 460 && y >= 100 && y <= 520) {
                //就近落子
                x = (x + 35) / 30 - 2;
                y = (y + 35) / 30 - 4;

                if (allChess[y][x] == 0) {
                    if (isBlack) {
                        allChess[y][x] = 1;
                        isBlack = false;
                        message = "轮到白方";
                    } else {
                        allChess[y][x] = -1;
                        isBlack = true;
                        message = "轮到黑方";
                    }

                    position.push(new Pair<Integer, Integer>(y, x));//把落子顺序加入到栈中
                } else {
                    JOptionPane.showMessageDialog(this, "如此下棋不合规，请三思！！");
                }
                this.repaint();
                if (isTrue(y, x)) { //符合游戏胜利要求
                    isBlack = !isBlack;
                    isOk = false; //游戏结束，不能继续下棋
                    JOptionPane.showMessageDialog(this, "游戏结束" +
                            (allChess[y][x] == 1 ? "黑方赢" : "白方赢"));
                }
            }
        }
    }

    //    判断是否连成五子
    public boolean isTrue(int row, int column) {
        int color = allChess[row][column];
//        int[] count = {1,1,1,1};
//        int[] i = {1,1,1,1};
//
//        //横向判断
//        while(column+i[0]<15&&color==allChess[row][column+i[0]]){
//            count[0]++;
//            i[0]++;
//        }
//        i[0] = 1;
//        while(column-i[0]>=0&&color==allChess[row][column-i[0]]){
//            count[0]++;
//            i[0]++;
//        }
////        纵向判断
//        while(row+i[1]<15&&color==allChess[row+i[1]][column]){
//            count[1]++;
//            i[1]++;
//        }
//        i[1]=1;
//        while(row-i[1]>=0&&color==allChess[row-i[1]][column]){
//            count[1]++;
//            i[1]++;
//        }
////        正斜线
//        while(row+i[2]<15&&column+i[2]<15&&color==allChess[row+i[2]][column+i[2]]){
//            count[2]++;
//            i[2]++;
//        }
//        i[2]=1;
//        while(row-i[2]>=0&&column-i[2]>=0&&color==allChess[row-i[2]][column-i[2]]){
//            count[2]++;
//            i[2]++;
//        }
////        反斜线
//        while(row+i[3]<15&&column-i[3]>=0&&color==allChess[row+i[3]][column-i[3]]){
//            count[3]++;
//            i[3]++;
//        }
//        i[3]=1;
//        while(row-i[3]>=0&&column+i[3]<15&&color==allChess[row-i[3]][column+i[3]]){
//            count[3]++;
//            i[3]++;
//        }
//        for (int j = 0; j < 4; j++) {
//            if (count[j] >= 5)
//                return true;
//        }
//        return false;
        if (f.countChess(0, 1, color) >= 5) { //纵向
            return true;
        } else if (f.countChess(1, 0, color) >= 5) { //横向
            return true;
        } else if (f.countChess(1, 1, color) >= 5) //正斜线
            return true;
        else //反斜线
            return f.countChess(1, -1, color) >= 5;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    //重新开始游戏
    private void restartActionPerformed() {
        int result = JOptionPane.showConfirmDialog(null, "是否重新开始游戏？",
                "重新开始", JOptionPane.YES_NO_OPTION);
        if (result == 0) {
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    this.allChess[i][j] = 0;
                }
            }
//            重新初始化游戏时间
            bTime = wTime = (int) (maxTime * 60);
            if (isBlack)
                message = "黑方先行";
            else
                message = "白方先行";
            this.repaint();
            this.isOk = true;
//            唤醒线程
            synchronized (this) {
                notify();
            }
        }
    }

    //悔棋
    private void resActionPerformed() {
        boolean em = true; //棋盘无棋子
        for (int i = 0; i < 15; i++)
            for (int j = 0; j < 15; j++)
                if (allChess[i][j] != 0)
                    em = false;

        if (em) {
            position.clear();
        }
//        棋盘正常下棋
        if (position.empty() == false && isOk) {
            Pair<Integer, Integer> latest = (Pair<Integer, Integer>) position.peek();
            position.pop();
            allChess[latest.getKey()][latest.getValue()] = 0;

            isBlack = !isBlack;

            if (isBlack) {
                message = "轮到黑方";
            } else
                message = "轮到白方";
            this.repaint();
        }

    }

    //认输
    private void bowActionPerformed(ActionEvent evt) {
        if (isOk) {
            isOk = false;
            if (isBlack) {
                JOptionPane.showMessageDialog(this, "白方获胜",
                        "Victory", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "黑方获胜",
                        "Victory", JOptionPane.INFORMATION_MESSAGE);
            }
            restartActionPerformed();
        }
    }

    //游戏设置
    private void setActionPerformed(ActionEvent evt) {
        boolean flag = true;
        while (flag) { //输入的数据不合法，循环输入
            boolean isAlpha = false;
            String time = JOptionPane.showInputDialog(null, "输入倒计时的时间（分钟）", "时间设置",
                    JOptionPane.PLAIN_MESSAGE);
            if (time == null || time.equals("")) //未输入内容
                return;
            try {
                maxTime = Float.parseFloat(time);
            } catch (NumberFormatException n) {
                isAlpha = true;
                if (flag)
                    JOptionPane.showMessageDialog(this, "输入类型错误",
                            "error", JOptionPane.ERROR_MESSAGE);
            }
            if (!isAlpha)
                if (maxTime >= 0) { //合法输入
                    restartActionPerformed();
                    flag = false;
                } else {
                    JOptionPane.showMessageDialog(this, "请输入正数",
                            "attension", JOptionPane.INFORMATION_MESSAGE);
                }
            bTime = wTime = (int) (maxTime * 60);
            if (!flag && first)
                t.start();
            first = false;
        }
    }

    //退出
    private void exitActionPerfomed() {
        int result = JOptionPane.showConfirmDialog(null, "退出游戏？",
                "退出游戏", JOptionPane.YES_NO_OPTION);
        if (result == 0) {
            isOk = true;
            synchronized (this) {
                notify();
            }
            if (!t.isInterrupted())
                t.interrupt();
            System.exit(0);
        }
    }

    //游戏时间线程
    @Override
    public void run() {
        if (maxTime > 0) {
            while (true) {
                if (isOk) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.exit(0);
                    }
                    if (isBlack) {
                        bTime--;
                    } else {
                        wTime--;
                    }

                    this.repaint();

                    if (bTime == 0 && maxTime != 0) {
                        JOptionPane.showMessageDialog(this, "时间到，白方获胜");
                        isOk = false;
                    }
                    if (wTime == 0 && maxTime != 0) {
                        JOptionPane.showMessageDialog(this, "时间到，黑方获胜");
                        isOk = false;
                    }
                } else {//线程等待
                    try {
                        synchronized (this) {
                            wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}