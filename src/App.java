import javax.swing.JFrame;
public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth =360;
        int boardHeight = 640;
        // Frame CREATI0N and CONFIGURATION
        JFrame frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(boardWidth, boardHeight);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); 

        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack(); 
        frame.setVisible(true);
        flappyBird.requestFocusInWindow();
    }
}
               