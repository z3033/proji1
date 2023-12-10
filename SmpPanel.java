import java.awt.*;
import java.awt.event.*;

public class SmpPanel extends Frame implements ActionListener{
	Button b0,b1,b2,b3,b4,b5,b6,b7,b8,b9,clr,ans,ka,gen,jou,jo;
	TextField txt;
	String ss="",s="";
	int a,an;

	public static void main(String [] args){
		SmpPanel si =new SmpPanel();
	}
	SmpPanel(){
		super("Layout Test");
		setSize(300,200);

		setLayout(new BorderLayout());
		txt =new TextField();
		add(txt, BorderLayout.NORTH);
		Panel p_c=new Panel();
		p_c.setLayout(new GridLayout(4,4));
		b1=new Button("1"); p_c.add(b1); b2=new Button("2"); p_c.add(b2);
		b3=new Button("3"); p_c.add(b3); b4=new Button("4"); p_c.add(b4);
		b5=new Button("5"); p_c.add(b5); b6=new Button("6"); p_c.add(b6);
		b7=new Button("7"); p_c.add(b7); b8=new Button("8"); p_c.add(b8);
		b9=new Button("9"); p_c.add(b9); b0=new Button("0"); p_c.add(b0); 
		clr=new Button("clr"); p_c.add(clr); ans=new Button("="); p_c.add(ans);
		ka=new Button("+"); p_c.add(ka); gen=new Button("-"); p_c.add(gen);
		jou=new Button("*"); p_c.add(jou); jo=new Button("%"); p_c.add(jo);
		b1.addActionListener(this);	b2.addActionListener(this);
		b3.addActionListener(this);	clr.addActionListener(this);
		b4.addActionListener(this);	b5.addActionListener(this);
		b6.addActionListener(this);	ka.addActionListener(this);
		b7.addActionListener(this);	b8.addActionListener(this);
		b0.addActionListener(this);	b9.addActionListener(this);
		gen.addActionListener(this); jou.addActionListener(this);
		jo.addActionListener(this);	ans.addActionListener(this);
		add(p_c,BorderLayout.CENTER);
		setVisible(true);	
	}
	public void actionPerformed(ActionEvent e){
		Button btn =(Button)e.getSource();
		if (btn==clr){ 
			ss=""; s="";
			a=0; an=0;
		}
		else if ((btn!=ka)&&(btn!=gen)&&(btn!=jou)&&(btn!=jo)&&(btn!=ans)){
			ss=ss+btn.getLabel();
			s=ss;
			if (a==0){
				an=Integer.parseInt(s);
			}
		}
		if (btn==ka){
			a=1; ss="";
		}
		if ((btn==gen)){
			a=2; ss="";
		}
		if((btn==jou)){
			a=3; ss="";
		}
		if((btn==jo)){
			a=4; ss="";
		}
		if(btn==ans){
			if(a==1) an=an+Integer.parseInt(s);
			if(a==2) an=an-Integer.parseInt(s);
			if(a==3) an=an*Integer.parseInt(s);
			if(a==4) an=an/Integer.parseInt(s);
			ss=""+an+"";
			s="";
		}
		update(this.getGraphics());
	}
	public void paint(Graphics g){
		txt.setText(ss);
	}

}
