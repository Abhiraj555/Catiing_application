
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.swing.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.*;

public class ClientBabti  implements ActionListener {

   static  JFrame f = new JFrame();
    private static final String TITLE = "My First Media Player";
    JTextField text;
   static JPanel a1;
   static Box vertical = Box.createVerticalBox();
    static DataOutputStream dout;

    //This is a constructor 
    ClientBabti(String titel) {
        f = new JFrame("My First Media Player");
        f.setLayout(null);
        JPanel p1 = new JPanel();
        p1.setLayout(null);
        p1.setBackground(Color.darkGray);
        p1.setBounds(0, 0, 440, 30);
        f.add(p1);

        JLabel name = new JLabel("Exit");
        name.setBounds(10, 5, 100, 18);
        name.setForeground(Color.white);
        p1.add(name);

        name.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        JLabel call = new JLabel("Katne Wali");
        call.setBounds(321, 5, 100, 18);
        call.setForeground(Color.white);
        p1.add(call);

        a1 = new JPanel();
        a1.setBounds(0, 30, 398, 500);
        f.add(a1);

        text = new JTextField();
        text.setBounds(0, 530, 315, 40);
        text.setFont(new Font("SAN_SERIF ", Font.PLAIN, 16));
        f.add(text);

        JButton send = new JButton("Send");
        send.setBounds(315, 530, 85, 40);
        send.setBackground(Color.GREEN);
        send.setForeground(Color.white);
        send.addActionListener(this);
        f.add(send);

        f.setSize(400, 570);
        f.setLocation(800, 50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.BLACK);
        f.setVisible(true);
    }

    public static void main(String[] args) {

        try {
//           if you create the object the its suddenly call the construtor 
            new ClientBabti(TITLE);

            Socket s = new Socket("127.0.0.1", 9806);
            DataInputStream din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            while (true) {
                a1.setLayout(new BorderLayout());
                String msg = din.readUTF();
                JPanel panel = formatLabel(msg);

                JPanel left = new JPanel(new BorderLayout());
                left.add(panel, BorderLayout.LINE_START);
                vertical.add(left);
                
                vertical.add(Box.createVerticalStrut(15));
                a1.add(vertical,BorderLayout.PAGE_START);
                f.validate();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style=\"width:150px\">" + out + "</html>");
        output.setFont(new Font("tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(37, 200, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50));

        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        panel.add(time);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String out = text.getText();

        JPanel p2 = formatLabel(out);

        a1.setLayout(new BorderLayout());
        JPanel right = new JPanel(new BorderLayout());
        right.add(p2, BorderLayout.LINE_END);//ye string nahi leta hai "out" ishke liye jlabel banana padega
        vertical.add(right);
        vertical.add(Box.createVerticalStrut(10));
        a1.add(vertical, BorderLayout.PAGE_START);

       try {
           dout.writeUTF(out);
       } catch (IOException ex) {
           Logger.getLogger(ClientBabti.class.getName()).log(Level.SEVERE, null, ex);
       }
        text.setText("");

        f.repaint();
        f.invalidate();
        f.validate();
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
