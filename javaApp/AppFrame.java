import javax.swing.JFrame;

public class AppFrame extends JFrame {
    AppFrame(){
        initApp();
    }

    private void initApp(){
        setTitle("MOHIT");
        setSize(500,500);
        setLocationRelativeTo(null);
        
        AppPanel ap = new AppPanel();
        add(ap);
        // setResizable(false);
        setVisible(true);
    }
}
