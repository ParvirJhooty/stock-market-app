import javax.swing.*;
import java.awt.event.*;
import org.json.JSONObject;



public class homepage {
    private JFrame mainWindow;
    private JTabbedPane tabbedPane;
    private StockSearch stocksearch;

    public homepage(String apiKey) {

        stocksearch = new StockSearch(apiKey);

        // Create the login panel
        JPanel loginPanel = new JPanel();
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);

        // Create the main window
        mainWindow = new JFrame("Login Tabbed Window");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(500, 500);

        // Create the tabbed pane
        tabbedPane = new JTabbedPane();

        // Add the login panel to the tabbed pane as the first tab
        tabbedPane.addTab("Login", null, loginPanel, "Login");

        // Add action listener to login button
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.equals("admin") && password.equals("password")) {

                JPanel searchPanel = new JPanel();
                JLabel searchSymbolLabel = new JLabel("Symbol:");
                JTextField searchSymbolTextField = new JTextField(10);
                JButton searchButton = new JButton("Search");

                // Add action listener to search button
                searchButton.addActionListener(e2 -> {
                    String symbol = searchSymbolTextField.getText();
                    JSONObject stockInfo = stocksearch.getStockInfo(symbol);
                
                    if (stockInfo == null) {
                        JOptionPane.showMessageDialog(mainWindow, "Invalid ticker symbol.");
                    } else {
                        // Display the stock info in a pop-up message
                        String output = String.format("Symbol: %s\nPrice: %s \nOpen: %s \nHigh: %s \nLow: %s \nVolume: %s \nLatest trading day: %s \nPrevious close: %s \nChange: %s \nChange Percent: %s", 
                        stockInfo.getString("symbol"), stockInfo.getString("price"), 
                        stockInfo.getString("open"), stockInfo.getString("high"),
                        stockInfo.getString("low"), stockInfo.getString("volume"),
                        stockInfo.getString("latest_trading_day"), stockInfo.getString("previous_close"),
                        stockInfo.getString("change"), stockInfo.getString("change_percent"));
                        JOptionPane.showMessageDialog(mainWindow, output);
                    }
                });
                
                // Add the components to the Search panel
                searchPanel.add(searchSymbolLabel);
                searchPanel.add(searchSymbolTextField);
                searchPanel.add(searchButton);
            
                
                // Create the Buy panel
                JPanel buyPanel = new JPanel();
                JLabel buySymbolLabel = new JLabel("Symbol:");
                JTextField buySymbolTextField = new JTextField(10);
                JLabel buyQuntityLabel = new JLabel("Qunatity:");
                JTextField buyQuntityTextField = new JTextField(10);
                JButton buyButton = new JButton("Buy");

                // Add action listener to Buy button
                buyButton.addActionListener(e1 -> {
                    String symbol = buySymbolTextField.getText();
                    double price = Double.parseDouble(buySymbolTextField.getText());

                    // Perform buy operation here...
                    JOptionPane.showMessageDialog(mainWindow, "Bought " + symbol + " at $" + price + ".");
                });

                // Add the components to the Buy panel
                buyPanel.add(buySymbolLabel);
                buyPanel.add(buySymbolTextField);
                buyPanel.add(buyQuntityLabel);
                buyPanel.add(buyQuntityTextField);
                buyPanel.add(buyButton);

                // Create the Sell panel
                JPanel sellPanel = new JPanel();
                JLabel sellSymbolLabel = new JLabel("Symbol:");
                JTextField sellSymbolTextField = new JTextField(10);
                JLabel sellPriceLabel = new JLabel("Price:");
                JTextField sellPriceTextField = new JTextField(10);
                JButton sellButton = new JButton("Sell");

                // Add action listener to Sell button
                sellButton.addActionListener(e1 -> {
                    String symbol = sellSymbolTextField.getText();
                    double price = Double.parseDouble(sellPriceTextField.getText());

                    // Perform sell operation here...
                    JOptionPane.showMessageDialog(mainWindow, "Sold " + symbol + " at $" + price + ".");
                });

                // Add the components to the Sell panel
                sellPanel.add(sellSymbolLabel);
                sellPanel.add(sellSymbolTextField);
                sellPanel.add(sellPriceLabel);
                sellPanel.add(sellPriceTextField);
                sellPanel.add(sellButton);

                // Create the Account Information panel
                JPanel accountInfoPanel = new JPanel();
                JLabel accountInfoLabel = new JLabel("This is the Account Information tab.");
                accountInfoPanel.add(accountInfoLabel);

                // Create the Portfolio panel
                JPanel portfolioPanel = new JPanel();
                JLabel portfolioLabel = new JLabel("This is the Portfolio tab.");
                portfolioPanel.add(portfolioLabel);

                // Create the Watchlist panel
                JPanel watchlistPanel = new JPanel();
                DefaultListModel<String> watchlistModel = new DefaultListModel<>();
                JList<String> watchlist = new JList<>(watchlistModel);
                JButton addToWatchlistButton = new JButton("Add to Watchlist");
                JButton removeFromWatchlistButton = new JButton("Remove from Watchlist");

                addToWatchlistButton.addActionListener(e1 -> {
                    String symbolToAdd = searchSymbolTextField.getText();
                    if (!watchlistModel.contains(symbolToAdd)) { // Check if symbol is already in watchlist
                        watchlistModel.addElement(symbolToAdd); // Add symbol to watchlist
                        StockSearch stockSearch = new StockSearch(symbolToAdd);
                        JSONObject stockInfo = stockSearch.getStockInfo(symbolToAdd);
                        if (stockInfo != null) {
                            double price = stockInfo.getDouble("price");
                            JOptionPane.showMessageDialog(mainWindow, symbolToAdd + " added to watchlist at current price: $" + price);
                        } else {
                            JOptionPane.showMessageDialog(mainWindow, "Could not retrieve stock information for " + symbolToAdd + ".");
                        }
                    } else {
                        JOptionPane.showMessageDialog(mainWindow, "Symbol is already in watchlist.");
                    }
                });

                JList<String> watchlist1 = new JList<>(watchlistModel);
                watchlist.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Check if item is double-clicked
                    String selectedSymbol = watchlist.getSelectedValue();
                    StockSearch stockSearch = new StockSearch(selectedSymbol);
                    JSONObject stockInfo = stockSearch.getStockInfo(selectedSymbol);
                if (stockInfo != null) {
                    double price = stockInfo.getDouble("price");
                    JOptionPane.showMessageDialog(mainWindow, selectedSymbol + " current price: $" + price);
                } else {
                    JOptionPane.showMessageDialog(mainWindow, "Could not retrieve stock information for " + selectedSymbol + ".");
                }
                }
                }
                });

                removeFromWatchlistButton.addActionListener(e1 -> {
                int selectedIndex = watchlist.getSelectedIndex();
                if (selectedIndex != -1) { // Check if item is selected
                    watchlistModel.remove(selectedIndex);
                } else {
                    JOptionPane.showMessageDialog(mainWindow, "Please select an item to remove.");
                }
                });

                // Add components to the Watchlist panel
                watchlistPanel.add(new JScrollPane(watchlist));
                watchlistPanel.add(addToWatchlistButton);
                watchlistPanel.add(removeFromWatchlistButton);

                // Add the panels to the tabbed pane and remove the login panel
                tabbedPane.addTab("Account Information", null, accountInfoPanel, "View Account Information");
                tabbedPane.addTab("Portfolio", null, portfolioPanel, "View Portfolio");
                tabbedPane.addTab("Search", null, searchPanel, "Search for Stock");
                tabbedPane.addTab("Watchlist", null, watchlistPanel, "Watchlist");
                tabbedPane.addTab("Buy", null, buyPanel, "Buy Stocks");
                tabbedPane.addTab("Sell", null, sellPanel, "Sell Stocks");
                
                
                tabbedPane.remove(loginPanel);
                              
            } else {
                JOptionPane.showMessageDialog(mainWindow, "Invalid username or password.");
            }
        });

        // Add the tabbed pane to the main window and display it as a pop-up window
        mainWindow.getContentPane().add(tabbedPane);
        mainWindow.setLocationRelativeTo(null); // Center the window on the screen
        mainWindow.setVisible(true);
    }

    public static void main(String[] args) {
        String apiKey = "D45VQXUV5P1KUVP5";
        new homepage(apiKey);
    }

    
}