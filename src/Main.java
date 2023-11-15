import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class Main extends JFrame implements ActionListener {
    private JPanel panel;
    private JPanel newPanel; // New panel for a fresh screen
    private boolean inUserLoginPanel = false;
    private Library library = new Library();
    JTextField usernameField;
    JPasswordField passwordField;
    JTextField enterUsernameField;
    JPasswordField enterPasswordField;
    JTextField enterLibrarianUsernameField;
    JPasswordField enterLibrarianPasswordField;

    private JButton optionsButton;
    private String password;
    private String userName;

    JButton searchButton;
    JButton goButton;

    JTextField searchField;

    private boolean searchByGenre = false;

    ArrayList<Book>results = new ArrayList<>();

    JPopupMenu popupMenu2;


    public static void main(String[] args) {
        Main app = new Main();
        app.setVisible(true);
    }

    public Main() {
        library.initializeUsers();
        library.initializeBooks();
        Librarian admin = new Librarian("Mr. Librarian", "admin".toCharArray(), "", "");
        User quick = new User("D", "davis123!".toCharArray(), "", "");
        library.addBook("Hello","World", "comedy", 2001);
        library.addBook("Bye", "Universe", "comedy", 2001);
        //library.users.add(quick);
        library.librarians.add(admin);
        this.setTitle("1337h4x0r.library.sjsu.ca.gov");
        this.setSize(600, 400);
        panel = startScreenPanel();
        this.add(panel);
    }

    //assigns actions to buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Log In")) {
            newPanel = createLoginPanel();
        } else if (e.getActionCommand().equals("Sign Up")) {
            newPanel = createSignupPanel();
        } else if (e.getActionCommand().equals("Go Back")) {
            if (inUserLoginPanel){
                newPanel = createLoginPanel();
            }
            else {
                newPanel = startScreenPanel();
            }
        } else if (e.getActionCommand().equals("User")) {
            newPanel = createUserLoginPanel();
        } else if (e.getActionCommand().equals("Admin")) {
            newPanel = createAdminLoginPanel();
        } else if (e.getActionCommand().equals("Sign-Up")){
            try {
                System.out.println("Signed Up Fields");
                //print statements so you remember user-name and password
                System.out.println("Username: " + usernameField.getText());
                System.out.println(passwordField.getPassword());
                library.passwordRequirement(passwordField.getPassword());
                //creates and adds user to library
                library.addUser(usernameField.getText(), passwordField.getPassword(), "", "");
                password = String.valueOf(passwordField.getPassword());
                userName = usernameField.getText();
                JOptionPane.showMessageDialog(null, "Sign In Successful!");

            } catch (PasswordException ex) {
                JOptionPane.showMessageDialog(null, "Password error: " + ex.getMessage());
            }
        } else if (e.getActionCommand().equals("Log-In")) {
            //print statements to check if you entered correct user-name and password
            System.out.println("Entered Fields");
            System.out.println("Username: " + enterUsernameField.getText());
            System.out.println(enterPasswordField.getPassword());
            //creates new unique user panel
            if (library.containsUserName(enterUsernameField.getText()) && library.containsPassword(enterPasswordField.getPassword())){
                User u = library.getUser(userName, password.toCharArray());
                if (u.hasPremium()){
                    newPanel = createPremiumUserPagePanel();
                } else {
                    newPanel = createUserPagePanel();
                }
            }
            else {
                System.out.println("Invalid");
            }

        } else if (e.getActionCommand().equals("Log-In [A]")) {
            if (enterLibrarianUsernameField.getText().equals("Mr. Librarian") && Arrays.equals(enterLibrarianPasswordField.getPassword(), "admin".toCharArray())){
                newPanel = createLibrarianPanel();
            }
        }
        else if (e.getActionCommand().equals("Options")) {
            //creates drop menu when options menu is pressed in user panel
            User u = library.getUser(userName, password.toCharArray());

            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem menuItem1 = new JMenuItem("Account Info");
            JMenuItem menuItem2 = new JMenuItem("Buy Premium");
            JMenuItem menuItem3 = new JMenuItem("Log Out");

            menuItem1.addActionListener(this);
            menuItem2.addActionListener(this);
            menuItem3.addActionListener(this);

            popupMenu.add(menuItem1);

            if (!u.hasPremium()){
                popupMenu.add(menuItem2);
            }
            popupMenu.add(menuItem3);

            popupMenu.show(optionsButton, 0, optionsButton.getHeight());

        } else if (e.getActionCommand().equals("Log Out")){
            newPanel = startScreenPanel();
        } else if (e.getActionCommand().equals("Account Info")){
            String enterPassword = JOptionPane.showInputDialog("Enter password to see account information");
            if (enterPassword.equals(password)){
                //displays username and password, add library card number
                showUserProfile(library.getUser(userName, password.toCharArray()));
            }
        } else if (e.getActionCommand().equals("Buy Premium")) {
            String upgradeToPremium = JOptionPane.showInputDialog("Upgrade to premium for $5 a month. Enter your password");
            if (upgradeToPremium.equals(password)){
                library.getUser(userName, password.toCharArray()).setPremium(true);
                JOptionPane.showMessageDialog(null, "Welcome to the premium club. Sign in again to access your premium account");
            }
        } else if (e.getActionCommand().equals("Search")) {
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem menuItem1 = new JMenuItem("Name");
            JMenuItem menuItem2 = new JMenuItem("Author");
            JMenuItem menuItem3 = new JMenuItem("Genre");
            JMenuItem menuItem4 = new JMenuItem("Year");

            menuItem1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    searchButton.setText("Search by Name");
                }
            });

            menuItem2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    searchButton.setText("Search by Author");
                }
            });

            menuItem3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    searchByGenre = true;
                    searchButton.setText("Search by Genre");
                }
            });

            menuItem4.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    searchButton.setText("Search by Year");
                }
            });

            popupMenu.add(menuItem1);
            popupMenu.add(menuItem2);
            popupMenu.add(menuItem3);
            popupMenu.add(menuItem4);

            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Show the drop-down menu when the search button is clicked
                    popupMenu.show(searchButton, 0, searchButton.getHeight());
                };
            });
        } else if (e.getActionCommand().equals("Go")){
            System.out.println("Detected");
            results.clear();

            if (searchButton.getText().equals("Search by Genre")){
                for (int i = 0; i < library.books.size(); i++){
                    if (library.books.get(i).getGenre().equals(searchField.getText())){
                        System.out.println("Added to result list");
                        results.add(library.books.get(i));
                    }
                }

                popupMenu2 = new JPopupMenu();

                for (int i = 0; i < results.size(); i++){
                    JMenuItem menuItemOne = new JMenuItem(results.get(i).toString());
                    menuItemOne.setPreferredSize(new java.awt.Dimension(300, 20));
                    int finalI = i;
                    menuItemOne.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            //add check out button to J Option pane that adds to users check out list
                            showBookInfo(results.get(finalI));
                        }
                    });
                    popupMenu2.add(menuItemOne);
                }
                System.out.println(results.size());

                popupMenu2.show(searchField, 0, searchField.getHeight());

            }
            if (searchButton.getText().equals("Search by Name")){
                for (int i = 0; i < library.books.size(); i++){
                    if (library.books.get(i).getName().equals(searchField.getText())){
                        System.out.println("Added to result list");
                        results.add(library.books.get(i));
                    }
                }

                popupMenu2 = new JPopupMenu();

                for (int i = 0; i < results.size(); i++){
                    JMenuItem menuItemOne = new JMenuItem(results.get(i).toString());
                    menuItemOne.setPreferredSize(new java.awt.Dimension(300, 20));
                    int finalI = i;
                    menuItemOne.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            //add check out button to J Option pane that adds to users check out list
                            showBookInfo(results.get(finalI));
                        }
                    });
                    popupMenu2.add(menuItemOne);
                }
                System.out.println(results.size());

                popupMenu2.show(searchField, 0, searchField.getHeight());

            }
            if (searchButton.getText().equals("Search by Author")){
                for (int i = 0; i < library.books.size(); i++){
                    if (library.books.get(i).getAuthor().equals(searchField.getText())){
                        System.out.println("Added to result list");
                        results.add(library.books.get(i));
                    }
                }

                popupMenu2 = new JPopupMenu();

                for (int i = 0; i < results.size(); i++){
                    JMenuItem menuItemOne = new JMenuItem(results.get(i).toString());
                    menuItemOne.setPreferredSize(new java.awt.Dimension(300, 20));
                    int finalI = i;
                    menuItemOne.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            //add check out button to J Option pane that adds to users check out list
                            showBookInfo(results.get(finalI));
                        }
                    });
                    popupMenu2.add(menuItemOne);
                }
                System.out.println(results.size());

                popupMenu2.show(searchField, 0, searchField.getHeight());

            }
            if (searchButton.getText().equals("Search by Year")){
                for (int i = 0; i < library.books.size(); i++){
                    if (String.valueOf(library.books.get(i).getYear()).equals(searchField.getText())){
                        System.out.println("Added to result list");
                        results.add(library.books.get(i));
                    }
                }

                popupMenu2 = new JPopupMenu();

                for (int i = 0; i < results.size(); i++){
                    JMenuItem menuItemOne = new JMenuItem(results.get(i).toString());
                    menuItemOne.setPreferredSize(new java.awt.Dimension(300, 20));
                    int finalI = i;
                    menuItemOne.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            //add check out button to J Option pane that adds to users check out list
                            showBookInfo(results.get(finalI));
                        }
                    });
                    popupMenu2.add(menuItemOne);
                }
                System.out.println(results.size());

                popupMenu2.show(searchField, 0, searchField.getHeight());

            }

        }

        this.remove(panel);
        panel = newPanel;
        this.add(panel);

        this.revalidate();
        this.repaint();
    }

    private JPanel createLibrarianPanel() {
        inUserLoginPanel = false;
        Rectangle rectangle = new Rectangle(0, 0, 600, 80);

        JPanel panel = createUpperBorderDisplay(rectangle, new Color(197, 160, 242));
        panel.setLayout(null);

        JLabel applicationLabel = createApplicationLabel(new Color(169, 138, 208));

        JLabel nameLabel = new JLabel("Librarian ");
        nameLabel.setBounds(250, 100, 400, 60);
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        nameLabel.setForeground(Color.white);

        optionsButton = new JButton("Log Out"); // Change from Options to Log out
        optionsButton.setBounds(475, 15, 100, 50);
        optionsButton.addActionListener(this);

        // Add buttons, each button is a function
        JButton addBookButton = new JButton("Add Book");
        addBookButton.setBounds(200,170,200,50);
        addBookButton.addActionListener(this);

        JButton removeBookButton = new JButton("Add User");
        removeBookButton.setBounds(200,240,200,50);
        removeBookButton.addActionListener(this);

        JButton removeUserButton = new JButton("Remove User");
        removeUserButton.setBounds(200,310,200,50);
        removeUserButton.addActionListener(this);

        JButton checkRequestButton = new JButton("Check Book Request");
        checkRequestButton.setBounds(200,380,200,50);
        checkRequestButton.addActionListener(this);

        JButton checkUserCheckOutButton = new JButton("Check User Checkout List");
        checkUserCheckOutButton.setBounds(200,450,200,50);
        checkUserCheckOutButton.addActionListener(this);

       /* JButton addNewAdminButton = new JButton("Add Admin");
        addNewAdminButton.setBounds(200,520,200,50);
        addNewAdminButton.add(this); */

        panel.add(addBookButton);
        panel.add(removeBookButton);
        panel.add(removeUserButton);
        panel.add(checkRequestButton);
        panel.add(checkUserCheckOutButton);
       // panel.add(addNewAdminButton);
        //End of add buttons


        panel.add(applicationLabel);
        panel.add(nameLabel);
        panel.add(optionsButton);

        return panel;
    }

    private JPanel startScreenPanel(){
        inUserLoginPanel = false;
        Rectangle rectangle = new Rectangle(0, 0, 600, 80);

        JPanel panel = createUpperBorderDisplay(rectangle, new Color(160, 235, 242));
        panel.setLayout(null);

        JLabel applicationLabel = createApplicationLabel(new Color(122, 183, 189));

        JButton loginButton = new JButton("Log In");
        loginButton.setBounds(360, 15, 100, 50);

        JButton signupButton = new JButton("Sign Up");
        signupButton.setBounds(475, 15, 100, 50);

        signupButton.addActionListener(this);
        loginButton.addActionListener(this);

        panel.add(applicationLabel);
        panel.add(loginButton);
        panel.add(signupButton);

        return panel;
    }

    //user page when you log in
    private JPanel createUserPagePanel() {
        inUserLoginPanel = false;
        Rectangle rectangle = new Rectangle(0, 0, 600, 80);

        JPanel panel = createUpperBorderDisplay(rectangle, new Color(197, 160, 242));
        panel.setLayout(null);

        JLabel applicationLabel = createApplicationLabel(new Color(169, 138, 208));

        JLabel nameLabel = new JLabel("Welcome " + usernameField.getText() + "!");
        nameLabel.setBounds(250, 100, 400, 60);
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        nameLabel.setForeground(Color.white);

        optionsButton = new JButton("Options");
        optionsButton.setBounds(475, 15, 100, 50);
        optionsButton.addActionListener(this);

        searchField = new JTextField(20);
        searchField.setBounds(215, 200, 265, 60);
        searchButton = new JButton("Search");
        searchButton.setBounds(100, 200, 120, 60);
        searchButton.addActionListener(this);
        goButton = new JButton("Go");
        goButton.setBounds(475, 200, 60, 60);
        goButton.addActionListener(this);


        panel.add(applicationLabel);
        panel.add(nameLabel);
        panel.add(optionsButton);
        panel.add(searchField);
        panel.add(searchButton);
        panel.add(goButton);

        return panel;


    }

    //premium user page when you log in
    private JPanel createPremiumUserPagePanel() {
        inUserLoginPanel = false;
        Rectangle rectangle = new Rectangle(0, 0, 600, 80);

        JPanel panel = createUpperBorderDisplay(rectangle, new Color(242, 206, 160));
        panel.setLayout(null);

        JLabel applicationLabel = createApplicationLabel(new Color(206, 166, 105));

        JLabel nameLabel = new JLabel("Premium User " + userName + "!");
        nameLabel.setBounds(250,100,400,60);
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        nameLabel.setForeground(Color.white);

        optionsButton = new JButton("Options");
        optionsButton.setBounds(475, 15, 100, 50);
        optionsButton.addActionListener(this);

        panel.add(applicationLabel);
        panel.add(nameLabel);
        panel.add(optionsButton);

        return panel;
    }


    private JPanel createLoginPanel() {
        inUserLoginPanel = false;
        Rectangle rectangle = new Rectangle(0, 0, 600, 80);

        JPanel loginPanel = createUpperBorderDisplay(rectangle, new Color(163, 242, 160));
        loginPanel.setLayout(null);

        JLabel applicationLabel = createApplicationLabel(new Color(130, 192, 128));
        JButton backButton = createBackButton();

        JButton adminstratorButton = new JButton("Admin");
        adminstratorButton.setBounds(310,150,160,120);
        adminstratorButton.addActionListener(this);

        JButton userButton = new JButton("User");
        userButton.setBounds(135,150,160,120);
        userButton.addActionListener(this);

        loginPanel.add(backButton);
        loginPanel.add(applicationLabel);
        loginPanel.add(adminstratorButton);
        loginPanel.add(userButton);
        return loginPanel;
    }

    private JPanel createUserLoginPanel(){
        inUserLoginPanel = true;
        Rectangle rectangle = new Rectangle(0, 0, 600, 80);

        JPanel userLoginPanel = createUpperBorderDisplay(rectangle, new Color(242, 197, 160));
        userLoginPanel.setLayout(null);

        JLabel applicationLabel = createApplicationLabel(new Color(204, 167, 136));

        JButton backButton = createBackButton();

        JButton loginButton = new JButton("Log-In");
        loginButton.setBounds(220, 280, 90, 45);
        loginButton.addActionListener(this);

        JLabel loginLabel = new JLabel("Log In");
        loginLabel.setBounds(218, 100, 100, 40);
        loginLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        loginLabel.setForeground(new Color(246, 243, 243));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(220, 145, 100, 30);

        enterUsernameField = new JTextField();
        enterUsernameField.setBounds(215, 175, 200, 30);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(220, 205, 100, 30);

        enterPasswordField = new JPasswordField();
        enterPasswordField.setBounds(215, 235, 200, 30);

        userLoginPanel.add(backButton);
        userLoginPanel.add(applicationLabel);
        userLoginPanel.add(usernameLabel);
        userLoginPanel.add(enterUsernameField);
        userLoginPanel.add(passwordLabel);
        userLoginPanel.add(enterPasswordField);
        userLoginPanel.add(loginLabel);
        userLoginPanel.add(loginButton);
        return userLoginPanel;
    }

    private JPanel createAdminLoginPanel(){
        inUserLoginPanel = true;
        Rectangle rectangle = new Rectangle(0, 0, 600, 80);

        JPanel adminLoginPanel = createUpperBorderDisplay(rectangle, new Color(242, 197, 160));
        adminLoginPanel.setLayout(null);

        JLabel applicationLabel = createApplicationLabel(new Color(204, 167, 136));

        JButton backButton = createBackButton();

        JButton loginButton = new JButton("Log-In [A]");
        loginButton.setBounds(220, 280, 90, 45);
        loginButton.addActionListener(this);

        JLabel loginLabel = new JLabel("Log In [Admin]");
        loginLabel.setBounds(218, 100, 170, 40);
        loginLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        loginLabel.setForeground(new Color(246, 243, 243));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(220, 145, 100, 30);

        enterLibrarianUsernameField = new JTextField();
        enterLibrarianUsernameField.setBounds(215, 175, 200, 30);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(220, 205, 100, 30);

        enterLibrarianPasswordField = new JPasswordField();
        enterLibrarianPasswordField.setBounds(215, 235, 200, 30);

        adminLoginPanel.add(backButton);
        adminLoginPanel.add(applicationLabel);
        adminLoginPanel.add(usernameLabel);
        adminLoginPanel.add(enterLibrarianUsernameField);
        adminLoginPanel.add(passwordLabel);
        adminLoginPanel.add(enterLibrarianPasswordField);
        adminLoginPanel.add(loginLabel);
        adminLoginPanel.add(loginButton);
        return adminLoginPanel;
    }

    private JPanel createSignupPanel() {
        inUserLoginPanel = false;

        Rectangle rectangle = new Rectangle(0, 0, 600, 80);

        JPanel signupPanel = createUpperBorderDisplay(rectangle, new Color(163, 242, 160));
        signupPanel.setLayout(null);

        JLabel applicationLabel = createApplicationLabel(new Color(130, 192, 128));
        JButton backButton = createBackButton();

        JButton signUpButton = new JButton("Sign-Up");
        signUpButton.setBounds(220, 280, 90, 45);
        signUpButton.addActionListener(this);

        JLabel signUpLabel = new JLabel("Sign Up");
        signUpLabel.setBounds(220, 100, 170, 40);
        signUpLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        signUpLabel.setForeground(new Color(246, 243, 243));

        JLabel usernameLabel = new JLabel("Enter Username:");
        usernameLabel.setBounds(220, 145, 150, 30);

        usernameField = new JTextField();
        usernameField.setBounds(215, 175, 200, 30);

        JLabel passwordLabel = new JLabel("Enter Password:");
        passwordLabel.setBounds(220, 205, 100, 30);

        passwordField = new JPasswordField();
        passwordField.setBounds(215, 235, 200, 30);

        signupPanel.add(backButton);
        signupPanel.add(applicationLabel);
        signupPanel.add(signUpLabel);
        signupPanel.add(usernameLabel);
        signupPanel.add(usernameField);
        signupPanel.add(passwordLabel);
        signupPanel.add(passwordField);
        signupPanel.add(signUpButton);
        return signupPanel;
    }

    private void showUserProfile(User user) {
        String name = user.getUsername();
        String userName = user.getUsername();
        String password = String.valueOf(user.getPassword());

        String profileMessage = "Name: " + name + "\n" + "Password: " + password;

        JOptionPane.showMessageDialog(this, profileMessage, "Profile", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showBookInfo(Book book) {
        String name = book.getName();
        String author = book.getAuthor();
        String genre = book.getGenre();
        String year = String.valueOf(book.getYear());

        String bookMessage = " Title: " + name + "\n" + "Author: " + author + "\n" + "Genre: " + genre + "\n" + "year: " + year;

        // Create an array of options (buttons)
        Object[] options = {"OK", "Check Out"};

        // Display the JOptionPane with a custom option panel
        int choice = JOptionPane.showOptionDialog(
                this,
                bookMessage,
                "Book",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        // Handle the user's choice
        if (choice == 1) {
            // User clicked "Check Out"
            // Add your checkout logic here
            JOptionPane.showMessageDialog(this, "Book Checked Out!");
        }
    }

    public JPanel createUpperBorderDisplay(Rectangle rectangle, Color c){
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            }
        };
        panel.setBackground(c);
        return panel;
    }

    private JLabel createApplicationLabel(Color c) {
        JLabel label = new JLabel("1337h4x0r Library");
        label.setBounds(30, 10, 400, 60);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        label.setForeground(c);
        return label;
    }

    private JButton createBackButton() {
        JButton button = new JButton("Go Back");
        button.setBounds(475, 15, 100, 50);
        button.addActionListener(this);
        return button;
    }

}
