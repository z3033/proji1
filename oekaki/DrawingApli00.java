import java.awt.*;
import java.awt.event.*;

public class DrawingApli00 extends Frame implements ActionListener, AdjustmentListener {
  // ■ フィールド変数
  Button bt1, bt2, bt3, bt4,bt5,bt6,btdel; // フレームに配置するボタンの宣言
  Panel  pnl,scpnl;                // ボタン配置用パネルの宣言
  MyCanvas mc;               // 別途作成した MyCanvas クラス型の変数の宣言
  Scrollbar sbar1,sbar2,sbar3,sbar4;
  Label lb1,lb2,lb3,lb4;
  
  int red=0,blue=0,green=0;   // drawing mode associated as below
  // ■ main メソッド（スタート地点）
  public static void main(String [] args) {
    DrawingApli00 da = new DrawingApli00(); 
  }

  // ■ コンストラクタ
  DrawingApli00() {
    super("Drawing Appli");
    this.setSize(600, 400); 
    scpnl=new Panel();
    pnl = new Panel();       // Panel のオブジェクト（実体）を作成
    mc = new MyCanvas(this); // mc のオブジェクト（実体）を作成

    this.setLayout(new BorderLayout(10, 10)); // レイアウト方法の指定
    this.add(pnl, BorderLayout.EAST);         // 右側に パネルを配置
    this.add(mc,  BorderLayout.CENTER);       // 左側に mc （キャンバス）を配置
                                         // BorerLayout の場合，West と East の幅は
                                         // 部品の大きさで決まる，Center は West と East の残り幅
    pnl.setLayout(new GridLayout(8,1));  // ボタンを配置するため，９行１列のグリッドをパネル上にさらに作成
    bt1 = new Button("Free Hand"); bt1.addActionListener(this); pnl.add(bt1);// ボタンを順に配置
    bt2 = new Button("Line");      bt2.addActionListener(this); pnl.add(bt2);
    bt3 = new Button("Rectangle"); bt3.addActionListener(this); pnl.add(bt3);
    bt4 = new Button("Oval");      bt4.addActionListener(this); pnl.add(bt4);
    bt5 = new Button("fillOval");  bt5.addActionListener(this); pnl.add(bt5);
    bt6 = new Button("fillrect");  bt6.addActionListener(this); pnl.add(bt6);
    btdel=new Button("delete");	   btdel.addActionListener(this); pnl.add(btdel);
    scpnl.setLayout(new GridLayout(8,1));
    lb1 = new Label("値", Label.CENTER); 
    scpnl.add(lb1); // ラベルを配置

    sbar1 = new Scrollbar(Scrollbar.HORIZONTAL, 50, 10, 0, 200);
    sbar1.addAdjustmentListener(this); 
    scpnl.add(sbar1); // スクロールバーを配置

    lb2 = new Label("赤", Label.CENTER); 
    scpnl.add(lb2); // ラベルを配置

    sbar2 = new Scrollbar(Scrollbar.HORIZONTAL, 0, 1, 0, 256);
    sbar2.addAdjustmentListener(this); 
    scpnl.add(sbar2); // スクロールバーを配置

    lb3 = new Label("緑", Label.CENTER); 
    scpnl.add(lb3); // ラベルを配置

    sbar3 = new Scrollbar(Scrollbar.HORIZONTAL, 0, 1, 0, 256);
    sbar3.addAdjustmentListener(this); 
    scpnl.add(sbar3); // スクロールバーを配置

    lb4 = new Label("青", Label.CENTER); 
    scpnl.add(lb4); // ラベルを配置

    sbar4 = new Scrollbar(Scrollbar.HORIZONTAL, 0, 1, 0, 256);
    sbar4.addAdjustmentListener(this); 
    scpnl.add(sbar4);
    pnl.add(scpnl);
    this.setVisible(true); //可視化
  }
  // ■ メソッド
  // ActionListener を実装しているため、例え内容が空でも必ず記述しなければならない
  public void actionPerformed(ActionEvent e){ // フレーム上で生じたイベントを e で取得
    if (e.getSource() == bt1)      // もしイベントが bt1 で生じたなら
      mc.mode=1;                   // モードを１に
    else if (e.getSource() == bt2) // もしイベントが bt2 で生じたなら
      mc.mode=2;                   // モードを２に
    else if (e.getSource() == bt3) // もしイベントが bt3 で生じたなら
      mc.mode=3;                   // モードを３に
    else if (e.getSource() == bt4) // もしイベントが bt4 で生じたなら
      mc.mode=4;                   // モードを４に
    else if (e.getSource() == btdel){
      mc.mode=5;
      mc.repaint();
    }
    else if (e.getSource() == bt5)
      mc.mode=6;
    else if (e.getSource() == bt6)
      mc.mode=7;
  }

  public void adjustmentValueChanged(AdjustmentEvent e){
     Scrollbar sbar=(Scrollbar)e.getAdjustable();
     if (sbar == sbar2 || sbar == sbar3 ||sbar == sbar4) {
       mc.red   = sbar2.getValue();
       mc.green = sbar3.getValue();
       mc.blue  = sbar4.getValue();
    }
   mc.repaint();	
  }
}

/**
 * Extended Canvas class for DrawingApli
 * [各モードにおける処理内容]
 * 1: free hand 
 *      pressed -> set x, y,  dragged  -> drawline & call repaint()
 * 2: draw line 
 *      pressed -> set x, y,  released -> drawline & call repaint()
 * 3: rect
 *      pressed -> set x, y,  released -> calc w, h & call repaint()
 * 4: circle
 *      pressed -> set x, y,  released -> calc w, h & call repaint()
 * 
 * @author fukai
 */
class MyCanvas extends Canvas implements MouseListener, MouseMotionListener {
  // ■ フィールド変数
  int x, y;   // mouse pointer position
  int px, py; // preliminary position
  int ow, oh; // width and height of the object
  int mode,red=0,blue=0,green=0;   // drawing mode associated as below
  Image img = null;   // 仮の画用紙
  Graphics gc = null; // 仮の画用紙用のペン
  Dimension d; // キャンバスの大きさ取得用

  // ■ コンストラクタ
  MyCanvas(DrawingApli00 obj){
    mode=0;                       // initial value 
    this.setSize(500,400);        // キャンバスのサイズを指定
    addMouseListener(this);       // マウスのボタンクリックなどを監視するよう指定
    addMouseMotionListener(this); // マウスの動きを監視するよう指定
  }

  // ■ メソッド（オーバーライド）
  // フレームに何らかの更新が行われた時の処理
  public void update(Graphics g) {
    paint(g); // 下記の paint を呼び出す
  }
 // public void paint(int r,int g, int b){
   // if (img == null) // もし仮の画用紙の実体がまだ存在しなければ
     // img = createImage(d.width, d.height); // 作成
    //if (gc == null)  // もし仮の画用紙用のペン (GC) がまだ存在しなければ
     // gc = img.getGraphics(); // 作成
    //gc.setColor(new Color(r,g,b));
    //gc.fillRect(0,0,getSize().width/20,getSize().height/20);
  //}
  // ■ メソッド（オーバーライド）
  public void paint(Graphics g) {
    d = getSize();   // キャンバスのサイズを取得
    //gc.setColor(new Color(red,green,blue));
    ///gc.fillRect(5,5,getSize().width-20,getSize().height-160);
    if (img == null) // もし仮の画用紙の実体がまだ存在しなければ
      img = createImage(d.width, d.height); // 作成
    if (gc == null)  // もし仮の画用紙用のペン (GC) がまだ存在しなければ
      gc = img.getGraphics(); // 作成
    switch (mode){
    case 1: // モードが１の場合
      gc.drawLine(px, py, x, y); // 仮の画用紙に描画
      break;
    case 2: // モードが２の場合
      gc.drawLine(px, py, x, y); // 仮の画用紙に描画
      break;
    case 3: // モードが３の場合
      gc.drawRect(px, py, ow, oh); // 仮の画用紙に描画
      break;
    case 4: // モードが４の場合
      gc.drawOval(px, py, ow, oh); // 仮の画用紙に描画
      break;
    case 5:
      gc.setColor(new Color(255,255,255));
      gc.fillRect(0,0,getSize().width,getSize().height);
      break;
      
    case 6: // モードが４の場合
      gc.fillOval(px, py, ow, oh); // 仮の画用紙に描画
      break;
    case 7: // モードが４の場合
      gc.fillRect(px, py, ow, oh); // 仮の画用紙に描画
      break;
    }

    gc.setColor(new Color(red,green,blue));
    gc.fillRect(0,0,getSize().width/50, getSize().height/40);
   
    g.drawImage(img, 0, 0, this); // 仮の画用紙の内容を MyCanvas に描画
  }

  // ■ メソッド
  // 下記のマウス関連のメソッドは，MouseListener をインターフェースとして実装しているため
  // 例え使わなくても必ず実装しなければならない
  public void mouseClicked(MouseEvent e){}// 今回は使わないが、無いとコンパイルエラー
  public void mouseEntered(MouseEvent e){}// 今回は使わないが、無いとコンパイルエラー
  public void mouseExited(MouseEvent e){} // 今回は使わないが、無いとコンパイルエラー
  public void mousePressed(MouseEvent e){ // マウスボタンが押された時の処理
    switch (mode){
    case 1: // mode が１の場合，次の内容を実行する
      x = e.getX();
      y = e.getY();
      break;
    case 2: // mode が２もしくは
    case 3: // ３もしくは
    case 4: // ４の場合，次の内容を実行する
    case 6:
    case 7:
      px = e.getX();
      py = e.getY();
    }
  }
  public void mouseReleased(MouseEvent e){ // マウスボタンが離された時の処理
    switch (mode){
    case 2: // mode が２もしくは
    case 3: // ３もしくは
    case 4: // ４の場合，次の内容を実行する
    case 6:
    case 7:
      x = e.getX();
      y = e.getY();
      ow = x-px;
      oh = y-py;
      if(x<px){
	px=x;
	ow=-ow;
      }
      if(y<py){
	py=y;
	oh=-oh;
      }
      repaint(); // 再描画
    }
  }

  // ■ メソッド
  // 下記のマウス関連のメソッドは，MouseMotionListener をインターフェースとして実装しているため
  // 例え使わなくても必ず実装しなければならない
  public void mouseDragged(MouseEvent e){ // マウスがドラッグされた時の処理
    switch (mode){
    case 1: // mode が１の場合，次の内容を実行する
      px = x;
      py = y;
      x = e.getX();
      y = e.getY();
      repaint(); // 再描画
    }
  }
  public void mouseMoved(MouseEvent e){} // 今回は使わないが、無いとコンパイルエラー
}

