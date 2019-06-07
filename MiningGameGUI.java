import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.TimerTask;
import java.util.Timer;

/**
 * Created by elitt on 5/22/2019.
 * Program Description:
 * Resources Used:
 * Worked With:
 */
public class MiningGameGUI extends JFrame
{
  /**
   * JLabels:
   */
  JLabel copper;
  JLabel iron;
  JLabel gold;
  JLabel coal;
  JLabel allMoney;
  JLabel availMoney;
  JLabel todayMoney;
  JLabel upgradePrice;
  JLabel pickLevel;
  JLabel oneStar;
  JLabel twoStar;
  JLabel threeStar;
  JLabel fourStar;
  JLabel fiveStar;
  JLabel gameOver;
  JLabel time;
  JLabel score;

  /**
   * The value of 0 in the array makes the space empty
   * The value of 1 in the array makes the space with stone
   * The value of 2 in the array makes the space with copper
   * The value of 3 in the array makes the space with iron
   * The value of 4 in the array makes the space with gold
   * The value of 5 in the array makes the space with the character
   */
  private int[][] arr;
  private Rock[][] rocks;
  private String[][] url;
  private JLabel[][] display;
  private JLabel[] staminaBars;
  private JPanel panel1;
  private JPanel panel2;
  private JPanel panel3;
  private JPanel panel4;
  private MyPanel panel5;
  private java.util.Timer timer;
  private TimerTask staminaBarT;
  private TimerTask clockT;
  private int xCoord = 0;
  private int yCoord = 0;
  private final int STONE, COPPER, IRON, GOLD;
  private int availableMoney = 0;
  private int totalMoney = 0;
  private int moneyToday = 0;
  private int coalInTotal = 0;
  private int copperOreInTotal = 0;
  private int ironOreInTotal = 0;
  private int goldOreInTotal = 0;
  private int pickaxeLevel = 1;
  private int oneStarFoodTotal = 0;
  private int twoStarFoodTotal = 0;
  private int threeStarFoodTotal = 0;
  private int fourStarFoodTotal = 0;
  private int fiveStarFoodTotal = 0;
  private int staminaNumber = 11;
  private boolean hasLost = false;
  private int minutes = 0;
  private int seconds = -1;
  private double scoreInt = 0;
  private String displayPickaxeLevel = "Normal Pickaxe";
  private int displayPickaxeLevelInt = 1;
  private int pickaxeUpgradePrice;
  private boolean hasUpgraded = false;
  /** Direction it's facing. */
  private boolean left = false;
  private boolean up = false;
  private boolean down = false;
  private boolean right = true;

  public MiningGameGUI(int stoneNum, int copperNum, int ironNum, int goldNum) {
    arr = new int[14][14];
    pickaxeUpgradePrice = Rock.getUpgradePrice();
    STONE = stoneNum;
    COPPER = copperNum;
    IRON = ironNum;
    GOLD = goldNum;
    assignRandomValues(arr);
    url = new String[14][14];
    for(int i = 0; i < url.length; i++) {
      for(int x = 0; x < url[i].length; x++) {
        url[i][x] = imageFileName(i, x);
      }
    }
    rocks = new Rock[14][14];
    for(int i = 0; i < arr.length; i++) {
      for(int x = 0; x < arr[i].length; x++) {
        if(arr[i][x] == 1) {
          rocks[i][x] = new Rock(1, 150);
        }
        else if(arr[i][x] == 2) {
          rocks[i][x] = new Rock(2, 75);
        }
        else if(arr[i][x] == 3) {
          rocks[i][x] = new Rock(3, 150);
        }
        else if(arr[i][x] == 4) {
          rocks[i][x] = new Rock(4, 400);
        }
      }
    }
    display = new JLabel[14][14];
    for(int i = 0; i < 14; i++) {
      for(int x = 0; x < 14; x++) {
        display[i][x] = new JLabel();
      }
    }
    staminaBars = new JLabel[11];
    for(int i = 0; i < 11; i++) {
      staminaBars[i] = new JLabel();
    }
    initDisplay();
    repaint();
    timer = new Timer();
    staminaBarT = new StaminaBarTimer();
    clockT = new ClockTimer();
    timer.scheduleAtFixedRate(staminaBarT, 0, 6000);
    timer.scheduleAtFixedRate(clockT, 0, 1000);
  }
  public int[][] getIntArr() {
    return arr;
  }
  public void reassignUrlArray() {
    for(int i = 0; i < url.length; i++) {
      for(int x = 0; x < url[i].length; x++) {
        url[i][x] = imageFileName(i, x);
      }
    }
  }
  /**
   * Returns the image that corresponds to the input card.
   * Image names have the format "24px-[Space].png",
   * for example "24px-Empty.png"
   *
   * @param i row of the arr to get the image for
   * @param a col of the arr to get the image for
   * @return String representation of the image
   */
  private String imageFileName(int i, int a) {
    if(Math.abs(xCoord - i) > 2) {
      return "pictures/24px-Black.png";
    }
    if(Math.abs(yCoord - a) > 2) {
      return "pictures/24px-Black.png";
    }
    if(arr[i][a] != 5 && i == 0 && a == 0) {
      return "pictures/24px-Resting-Room.png";
    }
    if(arr[i][a] == 5 && i == 0 && a == 0) {
      return "pictures/24px-Character-Resting.png";
    }
    if (arr[i][a] == 0) {
      return "pictures/24px-Empty.png";
    }
    else if (arr[i][a] == 1) {
      return "pictures/24px-Stone-Rock.jpg";
    }
    else if (arr[i][a] == 2) {
      return "pictures/24px-Copper-Ore.jpg";
    }
    else if (arr[i][a] == 3) {
      return "pictures/24px-Iron-Ore.jpg";
    }
    else if (arr[i][a] == 4) {
      return "pictures/24px-Gold-Ore.jpg";
    }
    else{
      if(left) {
        return "pictures/24px-Character-Left.png";
      }
      else if(right) {
        return "pictures/24px-Character-Right.png";
      }
      else if(up) {
        return "pictures/24px-Character-Up.png";
      }
      else {
        return "pictures/24px-Character-Down.png";
      }
    }
  }
  /**
   * Assigns random values to the arr
   *
   * @param arr row of the arr to get the image for
   */
  public void assignRandomValues(int[][] arr) {
    for(int i = 0; i < arr.length; i++) {
      for(int a = 0; a < arr[i].length; a++) {
        arr[i][a] = 0;
      }
    }
    int valuesLeft = STONE;
    while(valuesLeft > 0) {
      int x = (int)(Math.random() * arr.length);
      int y = (int)(Math.random() * arr.length);
      while(arr[x][y] != 0) {
        x = (int)(Math.random() * arr.length);
        y = (int)(Math.random() * arr.length);
      }
      arr[x][y] = 1;
      valuesLeft--;
    }
    valuesLeft = COPPER;
    while(valuesLeft > 0) {
      int x = (int)(Math.random() * arr.length);
      int y = (int)(Math.random() * arr.length);
      while(arr[x][y] != 0) {
        x = (int)(Math.random() * arr.length);
        y = (int)(Math.random() * arr.length);
      }
      arr[x][y] = 2;
      valuesLeft--;
    }
    valuesLeft = IRON;
    while(valuesLeft > 0) {
      int x = (int)(Math.random() * arr.length);
      int y = (int)(Math.random() * arr.length);
      while(arr[x][y] != 0) {
        x = (int)(Math.random() * arr.length);
        y = (int)(Math.random() * arr.length);
      }
      arr[x][y] = 3;
      valuesLeft--;
    }
    valuesLeft = GOLD;
    while(valuesLeft > 0) {
      int x = (int)(Math.random() * arr.length);
      int y = (int)(Math.random() * arr.length);
      while(arr[x][y] != 0) {
        x = (int)(Math.random() * arr.length);
        y = (int)(Math.random() * arr.length);
      }
      arr[x][y] = 4;
      valuesLeft--;
    }
    arr[0][0] = 5;
  }
  /**
   * Draw the display.
   */
  public void repaint() {
    for (int k = 0; k < url.length; k++) {
      for(int i = 0; i < url[k].length; i++) {
        String cardImageFileName = imageFileName(k, i);
        URL imageURL = getClass().getResource(cardImageFileName);
        if (imageURL != null) {
          ImageIcon icon = new ImageIcon(imageURL);
          display[k][i].setIcon(icon);
          display[k][i].setVisible(true);
        }
        else {
          throw new RuntimeException(
              "Card image not found: \"" + cardImageFileName + "\"");
        }
      }
    }

    /**
     * Setting up the map in the middle, GAHHH
     */
    for(int i = 0; i < 14; i++) {
      for(int x = 0; x < 14; x++) {
        display[i][x].setBounds(50 + 24 * x, 200 + 24 * i, 24, 24);
        panel2.add(display[i][x]);
      }
    }
  }
  private void initDisplay()	{
    /**
     *Makes the panels that we're going to put everything on:
     * panel1 does the things with selling, ending day, counting ore, and showing money.
     * panel2 does the things with the map
     * panel4 does the things with upgrading the pickaxe, displaying the upgrade price, showing the
     * current level, and also showing the prices of each ore.
     * panel5 has the instruction
     */
    panel1 = new JPanel();
    panel2 = new JPanel();
    panel3 = new JPanel();
    panel4 = new JPanel();
    panel5 = new MyPanel();

    /**
     * Sets up the title and bounds of the frame
     */
    setTitle("Mining Game");
    setVisible(true);
    setBounds(300,100,800,800);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new GridLayout(1, 3));
    panel3.setLayout(new GridLayout(2, 1));
    panel3.add(panel4);
    panel3.add(panel5);

    /**
     * Adding in all the panels onto the JFrame and setting all the layouts to null
     * and having all of the panels be visible
     */
    getContentPane().add(panel1);
    getContentPane().add(panel2);
    getContentPane().add(panel3);
    panel1.setVisible(true);
    panel2.setVisible(true);
    panel3.setVisible(true);
    panel4.setVisible(true);
    panel5.setVisible(true);
    panel1.setLayout(null);
    panel2.setLayout(null);
    panel4.setLayout(null);
    panel5.setLayout(null);

    /**
     * Sets up the mouse listener and the FREAKING KEY BINDINGS
     */
    addMouseListener(new MyMouseListener());

    /**
     * The Key Binding for W:
     */
    InputMap im1 = panel1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    im1.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "go up");
    ActionMap ap1 = panel1.getActionMap();
    ap1.put("go up", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if(!hasLost) {
//        System.out.println("This worksssss (up)");
          move("W");
        }
      }
    });

    /**
     * The Key Binding for A:
     */
    InputMap im2 = panel1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    im2.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "go left");
    ActionMap ap2 = panel1.getActionMap();
    ap2.put("go left", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if(!hasLost) {
//        System.out.println("This worksssss (left)");
          move("A");
        }
      }
    });
    /**
     * The Key Binding for S:
     */
    InputMap im3 = panel1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    im3.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "go down");
    ActionMap ap3 = panel1.getActionMap();
    ap3.put("go down", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if(!hasLost) {
//        System.out.println("This worksssss (down)");
          move("S");
        }
      }
    });
    /**
     * The Key Binding for D:
     */
    InputMap im4 = panel1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    im4.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "go right");
    ActionMap ap4 = panel1.getActionMap();
    ap4.put("go right", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if(!hasLost) {
//        System.out.println("This worksssss (right)");
          move("D");
        }
      }
    });

    /**
     * Setting up the selling buttons:
     */
    JButton button1 = new JButton("Sell");
    button1.setVisible(true);
    button1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
//        System.out.println("This button (1) works!");
        if(!hasLost) {
          if (xCoord != 0 || yCoord != 0) {
            if (copperOreInTotal > 0) {
              copperOreInTotal--;
              moneyToday += 75;
              update();
            }
          } else {
            if (copperOreInTotal > 0) {
              copperOreInTotal--;
              availableMoney += 15;
              totalMoney += 15;
              update();
            }
          }
        }
      }
    });
    JButton button2 = new JButton("Sell");
    button2.setVisible(true);
    button2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
//        System.out.println("This button (2) works!");
        if(!hasLost) {
          if (xCoord != 0 || yCoord != 0) {
            if (ironOreInTotal > 0) {
              ironOreInTotal--;
              moneyToday += 150;
              update();
            }
          } else {
            if (ironOreInTotal > 0) {
              ironOreInTotal--;
              availableMoney += 30;
              totalMoney += 30;
              update();
            }
          }
        }
      }
    });
    JButton button3 = new JButton("Sell");
    button3.setVisible(true);
    button3.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(!hasLost) {
//        System.out.println("This button (3) works!");
          if (xCoord != 0 || yCoord != 0) {
            if (goldOreInTotal > 0) {
              goldOreInTotal--;
              moneyToday += 400;
              update();
            }
          } else {
            if (goldOreInTotal > 0) {
              goldOreInTotal--;
              availableMoney += 80;
              totalMoney += 80;
              update();
            }
          }
        }
      }
    });
    JButton button4 = new JButton("Sell");
    button4.setVisible(true);
    button4.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
//        System.out.println("This button (4) works!");
        if(!hasLost) {
          if (xCoord != 0 || yCoord != 0) {
            if (coalInTotal > 0) {
              coalInTotal--;
              moneyToday += 150;
              update();
            }
          } else {
            if (coalInTotal > 0) {
              coalInTotal--;
              availableMoney += 30;
              totalMoney += 30;
              update();
            }
          }
        }
      }
    });
    button1.setBounds(25, 175, 60, 25);
    button2.setBounds(25, 205, 60, 25);
    button3.setBounds(25, 235, 60, 25);
    button4.setBounds(25, 265, 60, 25);
    panel1.add(button1);
    panel1.add(button2);
    panel1.add(button3);
    panel1.add(button4);

    /**
     * Setting up the end day button
     */
    JButton endDay = new JButton("End Day");
    endDay.setVisible(true);
    endDay.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!hasLost) {
          if (xCoord == 0 && yCoord == 0) {
//        System.out.println("Ended Day");
            totalMoney += moneyToday;
            availableMoney += moneyToday;
            moneyToday = 0;
            displayPickaxeLevelInt = pickaxeLevel;
            if (displayPickaxeLevelInt == 1) {
              displayPickaxeLevel = "Normal Pickaxe";
            }
            if (displayPickaxeLevelInt == 2) {
              displayPickaxeLevel = "Copper Pickaxe";
            }
            if (displayPickaxeLevelInt == 3) {
              displayPickaxeLevel = "Iron Pickaxe";
            }
            if (displayPickaxeLevelInt == 4) {
              displayPickaxeLevel = "Gold Pickaxe";
            }
            pickaxeUpgradePrice = Rock.getUpgradePrice();
            assignRandomValues(arr);
            xCoord = 0;
            yCoord = 0;
            right = true;
            left = false;
            up = false;
            down = false;
            for (int i = 0; i < arr.length; i++) {
              for (int x = 0; x < arr[i].length; x++) {
                if (arr[i][x] == 1) {
                  rocks[i][x] = new Rock(1, 150);
                } else if (arr[i][x] == 2) {
                  rocks[i][x] = new Rock(2, 75);
                } else if (arr[i][x] == 3) {
                  rocks[i][x] = new Rock(3, 150);
                } else if (arr[i][x] == 4) {
                  rocks[i][x] = new Rock(4, 400);
                }
              }
            }
            reassignUrlArray();
            repaint();
            update();
            hasUpgraded = false;
            staminaNumber = 10;
            for (int i = 0; i < 10; i++) {
              staminaBars[i].setVisible(true);
            }
          }
        }
      }
    });
    endDay.setBounds(50, 500, 100, 50);
    panel1.add(endDay);

    /**
     * Setting up all the JLabels in panel1
     */
    JLabel label1 = new JLabel("Available Ore: ");
    copper = new JLabel("Copper: " + copperOreInTotal);
    iron = new JLabel("Iron: " + ironOreInTotal);
    gold = new JLabel("Gold: " + goldOreInTotal);
    coal = new JLabel("Coal: " + coalInTotal);
    allMoney = new JLabel("Total Money: $" + totalMoney + ".00");
    availMoney = new JLabel("Available Money: $" + availableMoney + ".00");
    todayMoney = new JLabel("Money made Today: $" + moneyToday + ".00");
    gameOver = new JLabel("GAME OVER");
    gameOver.setFont(new Font("game over", Font.BOLD, 30));
    gameOver.setForeground(Color.RED);
    label1.setVisible(true);
    copper.setVisible(true);
    iron.setVisible(true);
    gold.setVisible(true);
    coal.setVisible(true);
    allMoney.setVisible(true);
    availMoney.setVisible(true);
    todayMoney.setVisible(true);
    gameOver.setVisible(false);
    label1.setBounds(50, -100, 500, 500);
    copper.setBounds(100, 175, 500, 25);
    iron.setBounds(100, 205, 500, 25);
    gold.setBounds(100, 235, 500, 25);
    coal.setBounds(100, 265, 500, 25);
    allMoney.setBounds(35, 400, 500, 25);
    availMoney.setBounds(35, 430, 500, 25);
    todayMoney.setBounds(35, 460, 500, 25);
    gameOver.setBounds(50, 400, 500, 500);
    panel1.add(label1);
    panel1.add(copper);
    panel1.add(iron);
    panel1.add(gold);
    panel1.add(coal);
    panel1.add(allMoney);
    panel1.add(availMoney);
    panel1.add(todayMoney);
    panel1.add(gameOver);

    /**
     * Setting up the Upgrade button
     */
    JButton upgrade = new JButton("Upgrade");
    upgrade.setVisible(true);
    upgrade.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(!hasLost) {
//        System.out.println("Upgraded");
          if (!hasUpgraded && Rock.upgrade(test.getObject())) {
            pickaxeLevel++;
            hasUpgraded = true;
            update();
          }
        }
      }
    });
    upgrade.setBounds(100, 175, 90, 25);
    panel4.add(upgrade);

    /**
     * Setting up all the JLabels in panel4
     */
    upgradePrice = new JLabel("Upgrade Price: " + pickaxeUpgradePrice);
    pickLevel = new JLabel("Pickaxe Level: " + displayPickaxeLevel);
    JLabel orePrices = new JLabel("Ore Prices: ");
    JLabel copperPrice = new JLabel("Copper: 75");
    JLabel ironPrice = new JLabel("Iron: 150");
    JLabel goldPrice = new JLabel("Gold: 400");
    JLabel coalPrice = new JLabel("Coal: 150");
    upgradePrice.setVisible(true);
    pickLevel.setVisible(true);
    orePrices.setVisible(true);
    copperPrice.setVisible(true);
    ironPrice.setVisible(true);
    goldPrice.setVisible(true);
    coalPrice.setVisible(true);
    upgradePrice.setBounds(100, 205, 500, 25);
    pickLevel.setBounds(100, 220, 500, 25);
    orePrices.setBounds(100, 300, 200, 25);
    copperPrice.setBounds(125, 320, 200, 25);
    ironPrice.setBounds(125, 340, 200, 25);
    goldPrice.setBounds(125, 360, 200, 25);
    coalPrice.setBounds(125, 380, 200, 25);
    panel4.add(upgradePrice);
    panel4.add(pickLevel);
    panel4.add(orePrices);
    panel4.add(copperPrice);
    panel4.add(ironPrice);
    panel4.add(goldPrice);
    panel4.add(coalPrice);

    /**
     * Setting up stamina meter
     */
    JLabel stamina = new JLabel("Stamina");
    stamina.setVisible(true);
    stamina.setBounds(50, 600, 500, 100);
    panel2.add(stamina);
    for(int i = 0; i < 11; i++) {
      String cardImageFileName = "pictures/24px-Stamina-Bar.png";
      URL imageURL = getClass().getResource(cardImageFileName);
      if (imageURL != null) {
        ImageIcon icon = new ImageIcon(imageURL);
        staminaBars[i].setIcon(icon);
        staminaBars[i].setVisible(true);
      }
      else {
        throw new RuntimeException(
            "Card image not found: \"" + cardImageFileName + "\"");
      }
    }
    for(int i = 0; i < 11; i++) {
      staminaBars[i].setBounds(115 + 24 * i, 610, 24, 48);
        panel2.add(staminaBars[i]);
    }

    /**
     * Setting up the food buy buttons
     */
    JButton oneStarFoodBuy = new JButton("400");
    oneStarFoodBuy.setVisible(true);
    oneStarFoodBuy.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(!hasLost) {
//        System.out.println("This button (1) works!");
          if (availableMoney >= 400 && xCoord == 0 && yCoord == 0) {
            spendMoney(400);
            oneStarFoodTotal++;
            update();
          }
        }
      }
    });
    JButton twoStarFoodBuy = new JButton("750");
    twoStarFoodBuy.setVisible(true);
    twoStarFoodBuy.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
//        System.out.println("This button (1) works!");
        if(!hasLost) {
          if (availableMoney >= 750 && xCoord == 0 && yCoord == 0) {
            spendMoney(750);
            twoStarFoodTotal++;
            update();
          }
        }
      }
    });
    JButton threeStarFoodBuy = new JButton("975");
    threeStarFoodBuy.setVisible(true);
    threeStarFoodBuy.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
//        System.out.println("This button (1) works!");
        if(!hasLost) {
          if (availableMoney >= 975 && xCoord == 0 && yCoord == 0) {
            spendMoney(975);
            threeStarFoodTotal++;
            update();
          }
        }
      }
    });
    JButton fourStarFoodBuy = new JButton("1200");
    fourStarFoodBuy.setVisible(true);
    fourStarFoodBuy.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(!hasLost) {
//        System.out.println("This button (1) works!");
          if (availableMoney >= 1200 && xCoord == 0 && yCoord == 0) {
            spendMoney(1200);
            fourStarFoodTotal++;
            update();
          }
        }
      }
    });
    JButton fiveStarFoodBuy = new JButton("1450");
    fiveStarFoodBuy.setVisible(true);
    fiveStarFoodBuy.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(!hasLost) {
//        System.out.println("This button (1) works!");
          if (availableMoney >= 1450 && xCoord == 0 && yCoord == 0) {
            spendMoney(1450);
            fiveStarFoodTotal++;
            update();
          }
        }
      }
    });
    panel2.add(oneStarFoodBuy);
    panel2.add(twoStarFoodBuy);
    panel2.add(threeStarFoodBuy);
    panel2.add(fourStarFoodBuy);
    panel2.add(fiveStarFoodBuy);
    oneStarFoodBuy.setBounds(100, 700, 70, 25);
    twoStarFoodBuy.setBounds(100, 730, 70, 25);
    threeStarFoodBuy.setBounds(100, 760, 70, 25);
    fourStarFoodBuy.setBounds(100, 790, 70, 25);
    fiveStarFoodBuy.setBounds(100, 820, 70, 25);


    /**
     * Setting up the food eat buttons
     */
    JButton oneStarFoodEat = new JButton("Eat");
    oneStarFoodEat.setVisible(true);
    oneStarFoodEat.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(!hasLost) {
//       System.out.println("This button (1) works!");
          if (oneStarFoodTotal >= 1) {
            oneStarFoodTotal--;
            addStamina(1);
            update();
          }
        }
      }
    });
    JButton twoStarFoodEat = new JButton("Eat");
    twoStarFoodEat.setVisible(true);
    twoStarFoodEat.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(!hasLost) {
//       System.out.println("This button (1) works!");
          if (twoStarFoodTotal >= 1) {
            twoStarFoodTotal--;
            addStamina(2);
            update();
          }
        }
      }
    });
    JButton threeStarFoodEat = new JButton("Eat");
    threeStarFoodEat.setVisible(true);
    threeStarFoodEat.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(!hasLost) {
//       System.out.println("This button (1) works!");
          if (threeStarFoodTotal >= 1) {
            threeStarFoodTotal--;
            addStamina(3);
            update();
          }
        }
      }
    });
    JButton fourStarFoodEat = new JButton("Eat");
    fourStarFoodEat.setVisible(true);
    fourStarFoodEat.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(!hasLost) {
//        System.out.println("This button (1) works!");
          if (fourStarFoodTotal >= 1) {
            fourStarFoodTotal--;
            addStamina(4);
            update();
          }
        }
      }
    });
    JButton fiveStarFoodEat = new JButton("Eat");
    fiveStarFoodEat.setVisible(true);
    fiveStarFoodEat.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(!hasLost) {
//       System.out.println("This button (1) works!");
          if (fiveStarFoodTotal >= 1) {
            fiveStarFoodTotal--;
            addStamina(5);
            update();
          }
        }
      }
    });
    panel2.add(oneStarFoodEat);
    panel2.add(twoStarFoodEat);
    panel2.add(threeStarFoodEat);
    panel2.add(fourStarFoodEat);
    panel2.add(fiveStarFoodEat);
    oneStarFoodEat.setBounds(280, 700, 70, 25);
    twoStarFoodEat.setBounds(280, 730, 70, 25);
    threeStarFoodEat.setBounds(280, 760, 70, 25);
    fourStarFoodEat.setBounds(280, 790, 70, 25);
    fiveStarFoodEat.setBounds(280, 820, 70, 25);

    /**
     * Setting up the food JLabels
     */
    JLabel buyFood = new JLabel("Buy Food:");
    oneStar = new JLabel("1-Star Food: " + oneStarFoodTotal);
    twoStar = new JLabel("2-Star Food: " + twoStarFoodTotal);
    threeStar = new JLabel("3-Star Food: " + threeStarFoodTotal);
    fourStar = new JLabel("4-Star Food: " + fourStarFoodTotal);
    fiveStar = new JLabel("5-Star Food: " + fiveStarFoodTotal);
    buyFood.setVisible(true);
    oneStar.setVisible(true);
    twoStar.setVisible(true);
    threeStar.setVisible(true);
    fourStar.setVisible(true);
    fiveStar.setVisible(true);
    buyFood.setBounds(50, 430, 500, 500);
    oneStar.setBounds(175, 460, 500, 500);
    twoStar.setBounds(175, 490, 500, 500);
    threeStar.setBounds(175, 520, 500, 500);
    fourStar.setBounds(175, 550, 500, 500);
    fiveStar.setBounds(175, 580, 500, 500);
    panel2.add(buyFood);
    panel2.add(oneStar);
    panel2.add(twoStar);
    panel2.add(threeStar);
    panel2.add(fourStar);
    panel2.add(fiveStar);

    /**
     * Setting up the time at the bottom
     */
    time = new JLabel("Time:  00:00");
    time.setVisible(true);
    time.setBounds(50, 320, 500, 500);
    panel1.add(time);

    /**
     * Setting up the score
     */
    score = new JLabel("Score: " + scoreInt);
    score.setVisible(true);
    score.setBounds(50, 350, 500, 500);
    panel1.add(score);
  }
  public void setScore() {
    scoreInt = (totalMoney + moneyToday)/(minutes * 60 + seconds + 1);
    scoreInt = (int)(scoreInt * 100);
    scoreInt = scoreInt / 100;
    score.setText("Score: " + scoreInt);
  }
  public boolean move(String direction) {
//    System.out.println("xCoord is: " + xCoord);
//    System.out.println("yCoord is: " + yCoord);
    if(direction.equals("W")) {
      if(!hasLost) {
        left = false;
        right = false;
        up = true;
        down = false;
        if (xCoord >= 1) {
          if (arr[xCoord - 1][yCoord] == 0) {
            arr[xCoord - 1][yCoord] = 5;
            arr[xCoord][yCoord] = 0;
            xCoord--;
          }
        }
        reassignUrlArray();
        repaint();
        return true;
      }
    }
    else if(direction.equals("A")) {
      if(!hasLost) {
        left = true;
        right = false;
        up = false;
        down = false;
        if (yCoord >= 1) {
          if (arr[xCoord][yCoord - 1] == 0) {
            arr[xCoord][yCoord - 1] = 5;
            arr[xCoord][yCoord] = 0;
            yCoord--;
          }
        }
        reassignUrlArray();
        repaint();
        return true;
      }
    }
    else if(direction.equals("S")) {
      if(!hasLost) {
        left = false;
        right = false;
        up = false;
        down = true;
        if (xCoord < arr.length - 1) {
          if (arr[xCoord + 1][yCoord] == 0) {
            arr[xCoord + 1][yCoord] = 5;
            arr[xCoord][yCoord] = 0;
            xCoord++;
          }
        }
        reassignUrlArray();
        repaint();
        return true;
      }
    }
    else if(direction.equals("D")) {
      if (!hasLost) {
        left = false;
        right = true;
        up = false;
        down = false;
        if (yCoord < arr.length - 1) {
          if (arr[xCoord][yCoord + 1] == 0) {
            arr[xCoord][yCoord + 1] = 5;
            arr[xCoord][yCoord] = 0;
            yCoord++;
          }
        }
        reassignUrlArray();
        repaint();
        return true;
      }
    }
    return false;
  }
  public void changeSpaceToEmpty(int x, int y) {
    arr[x][y] = 0;
    reassignUrlArray();
    repaint();
  }
  public int getAvailableMoney() {
    return availableMoney;
  }
  public void spendMoney(int money) {
    availableMoney -= money;
  }
  public void update() {
    copper.setText("Copper: " + copperOreInTotal);
    iron.setText("Iron: " + ironOreInTotal);
    gold.setText("Gold: " + goldOreInTotal);
    coal.setText("Coal: " + coalInTotal);
    allMoney.setText("Total Money: $" + totalMoney + ".00");
    availMoney.setText("Available Money: $" + availableMoney + ".00");
    todayMoney.setText("Money made Today: $" + moneyToday + ".00");
    upgradePrice.setText("Upgrade Price: " + pickaxeUpgradePrice);
    pickLevel.setText("Pickaxe Level: " + displayPickaxeLevel);
    oneStar.setText("1-Star Food: " + oneStarFoodTotal);
    twoStar.setText("2-Star Food: " + twoStarFoodTotal);
    threeStar.setText("3-Star Food: " + threeStarFoodTotal);
    fourStar.setText("4-Star Food: " + fourStarFoodTotal);
    fiveStar.setText("5-Star Food: " + fiveStarFoodTotal);
  }
  public void removeStamina() {
    staminaNumber--;
    staminaBars[staminaNumber].setVisible(false);
    if(staminaNumber == 0) {
      endGame();
    }
  }
  public void addStamina(int numStaminaAdded) {
    if(staminaNumber + numStaminaAdded >= 10) {
      staminaNumber = 10;
    }
    else {
      staminaNumber += numStaminaAdded;
    }
    for(int i = 0; i < staminaNumber; i++) {
      staminaBars[i].setVisible(true);
    }
  }
  public void endGame() {
    staminaBarT.cancel();
    clockT.cancel();
    gameOver.setVisible(true);
    hasLost = true;
  }
  public void updateTime() {
    if(seconds >= 10 && minutes >= 10) {
      time.setText("Time:  " + minutes + ":" + seconds);
    }
    else if(seconds >= 10 && minutes < 10) {
      time.setText("Time:  0" + minutes + ":" + seconds);
    }
    else if(seconds < 10 && minutes >= 10) {
      time.setText("Time:  " + minutes + ":0" + seconds);
    }
    else {
      time.setText("Time:  0" + minutes + ":0" + seconds);
    }
  }
  public void addTime() {
    seconds++;
    if(seconds == 60) {
      seconds = 0;
      minutes++;
    }
    updateTime();
  }
  public class MyMouseListener implements MouseListener
  {
    public void mouseClicked(MouseEvent e) {
      if(!hasLost) {
//      System.out.println("Click!");
        if (left) {
          if (yCoord >= 1) {
            if (arr[xCoord][yCoord - 1] != 0 && arr[xCoord][yCoord - 1] <= displayPickaxeLevelInt + 1) {
              rocks[xCoord][yCoord - 1].reduceDurability();
              if (rocks[xCoord][yCoord - 1].getDurability() == 0) {
                if (arr[xCoord][yCoord - 1] == 1) {
                  coalInTotal += rocks[xCoord][yCoord - 1].breakRock(test.getObject(), xCoord, yCoord - 1);
                }
                if (arr[xCoord][yCoord - 1] == 2) {
                  copperOreInTotal += rocks[xCoord][yCoord - 1].breakRock(test.getObject(), xCoord, yCoord - 1);
                }
                if (arr[xCoord][yCoord - 1] == 3) {
                  ironOreInTotal += rocks[xCoord][yCoord - 1].breakRock(test.getObject(), xCoord, yCoord - 1);
                }
                if (arr[xCoord][yCoord - 1] == 4) {
                  goldOreInTotal += rocks[xCoord][yCoord - 1].breakRock(test.getObject(), xCoord, yCoord - 1);
                }
                test.getObject().update();
              }
            }
          }
        } else if (right) {
          if (yCoord <= 12) {
            if (arr[xCoord][yCoord + 1] != 0 && arr[xCoord][yCoord + 1] <= displayPickaxeLevelInt + 1) {
              rocks[xCoord][yCoord + 1].reduceDurability();
              if (rocks[xCoord][yCoord + 1].getDurability() == 0) {
                if (arr[xCoord][yCoord + 1] == 1) {
                  coalInTotal += rocks[xCoord][yCoord + 1].breakRock(test.getObject(), xCoord, yCoord + 1);
                }
                if (arr[xCoord][yCoord + 1] == 2) {
                  copperOreInTotal += rocks[xCoord][yCoord + 1].breakRock(test.getObject(), xCoord, yCoord + 1);
                }
                if (arr[xCoord][yCoord + 1] == 3) {
                  ironOreInTotal += rocks[xCoord][yCoord + 1].breakRock(test.getObject(), xCoord, yCoord + 1);
                }
                if (arr[xCoord][yCoord + 1] == 4) {
                  goldOreInTotal += rocks[xCoord][yCoord + 1].breakRock(test.getObject(), xCoord, yCoord + 1);
                }
                test.getObject().update();
              }
            }
          }
        } else if (up) {
          if (xCoord >= 1) {
            if (arr[xCoord - 1][yCoord] != 0 && arr[xCoord - 1][yCoord] <= displayPickaxeLevelInt + 1) {
              rocks[xCoord - 1][yCoord].reduceDurability();
              if (rocks[xCoord - 1][yCoord].getDurability() == 0) {
                if (arr[xCoord - 1][yCoord] == 1) {
                  coalInTotal += rocks[xCoord - 1][yCoord].breakRock(test.getObject(), xCoord - 1, yCoord);
                }
                if (arr[xCoord - 1][yCoord] == 2) {
                  copperOreInTotal += rocks[xCoord - 1][yCoord].breakRock(test.getObject(), xCoord - 1, yCoord);
                }
                if (arr[xCoord - 1][yCoord] == 3) {
                  ironOreInTotal += rocks[xCoord - 1][yCoord].breakRock(test.getObject(), xCoord - 1, yCoord);
                }
                if (arr[xCoord - 1][yCoord] == 4) {
                  goldOreInTotal += rocks[xCoord - 1][yCoord].breakRock(test.getObject(), xCoord - 1, yCoord);
                }
                test.getObject().update();
              }
            }
          }
        } else if (down) {
          if (xCoord <= 12) {
            if (arr[xCoord + 1][yCoord] != 0 && arr[xCoord + 1][yCoord] <= displayPickaxeLevelInt + 1) {
              rocks[xCoord + 1][yCoord].reduceDurability();
              if (rocks[xCoord + 1][yCoord].getDurability() == 0) {
                if (arr[xCoord + 1][yCoord] == 1) {
                  coalInTotal += rocks[xCoord + 1][yCoord].breakRock(test.getObject(), xCoord + 1, yCoord);
                }
                if (arr[xCoord + 1][yCoord] == 2) {
                  copperOreInTotal += rocks[xCoord + 1][yCoord].breakRock(test.getObject(), xCoord + 1, yCoord);
                }
                if (arr[xCoord + 1][yCoord] == 3) {
                  ironOreInTotal += rocks[xCoord + 1][yCoord].breakRock(test.getObject(), xCoord + 1, yCoord);
                }
                if (arr[xCoord + 1][yCoord] == 4) {
                  goldOreInTotal += rocks[xCoord + 1][yCoord].breakRock(test.getObject(), xCoord + 1, yCoord);
                }
                test.getObject().update();
              }
            }
          }
        }
//      System.out.println("Coal: " + coalInTotal);
//      System.out.println("Copper: " + copperOreInTotal);
//      System.out.println("Iron: " + ironOreInTotal);
//      System.out.println("Gold: " + goldOreInTotal);
//      System.out.println("--------------------------");
      }
    }

    /**
     * Ignore a mouse exited event.
     * @param e the mouse event.
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Ignore a mouse released event.
     * @param e the mouse event.
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Ignore a mouse entered event.
     * @param e the mouse event.
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Ignore a mouse pressed event.
     * @param e the mouse event.
     */
    public void mousePressed(MouseEvent e) {
    }
  }
  public class MyPanel extends JPanel {
    public void paint(Graphics g) {
      g.fillRect(50, 0, 350, 365);
      g.clearRect(55, 5, 340, 355);
      g.drawString("Instructions:", 60, 20);
      g.drawString("Use the WASD keys to move around.  Please do not hold", 60, 35);
      g.drawString("them.  It can cause the game to slow down.  Also, you can", 60, 50);
      g.drawString("click the left-mouse button to mine a rock.  A normal rock has", 60, 65);
      g.drawString("a durability of 1.  If you don't see the keyboard or click", 60, 80);
      g.drawString("responding, please wait.  It is catching up.  Click or try to", 60, 95);
      g.drawString("move again after a while.  A copper ore rock has a", 60, 110);
      g.drawString("durability of 2, an iron ore rock has a durability of 3, and", 60, 125);
      g.drawString("a gold ore rock has a durability of 3.  Each mouse click takes", 60, 140);
      g.drawString("off one durability.  When the durability reaches 0, the rock", 60, 155);
      g.drawString("breaks.  When a rock breaks, a random number of that ore is", 60, 170);
      g.drawString(" collected.  The minimum is 0, and the maximum is 3.  You", 60, 185);
      g.drawString("can also sell that ore for the price listed above.  You will only", 60, 200);
      g.drawString("get the money at the end of the day.  You can also upgrade", 60, 215);
      g.drawString("your pickaxe.  In the beginning, you will only have a normal", 60, 230);
      g.drawString("pickaxe, meaning that you can only break copper and normal", 60, 245);
      g.drawString("rocks.  If you upgrade it to a copper pickaxe, you can now", 60, 260);
      g.drawString("mine iron, copper, and normal rocks.  If you upgrade it again", 60, 275);
      g.drawString("to an iron pickaxe, you can now mine all the rocks. Upgrading", 60, 290);
      g.drawString("it one last time allows you to have a higher chance to get 3", 60, 305);
      g.drawString("ore per rock.", 60, 320);
      g.drawString("                                           Enjoy playing!", 60, 350);
    }
  }
}