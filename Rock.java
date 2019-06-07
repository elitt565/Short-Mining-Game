public class Rock
{
    private static int level = 1;
    private static boolean isGoldPickaxe = false;
    private int durability;
    private int price;
    public Rock(int dur, int p) {
        durability = dur;
        price = p;
    }
    public int getAmount() {
        if(!isGoldPickaxe) {
            return (int)(Math.random() * 4);
        }
        else {
            int i = (int)(Math.random() * 6);
            if(i >= 3) {
                return 3;
            }
            else {
                return i;
            }
        }
    }
    public int getDurability() {
        return durability;
    }
    public void reduceDurability() {
        durability--;
    }
    public int getPrice() {
        return price;
    }
    public static int getUpgradePrice() {
        if(level == 1) {
            return 5000;
        }
        if(level == 2) {
            return 15000;
        }
        if(level == 3) {
            return 40000;
        }
        else {
            return 0;
        }
    }
    public int breakRock(MiningGameGUI display, int xCoord, int yCoord) {
        /**
         * Change space to empty
         */
        display.changeSpaceToEmpty(xCoord, yCoord);
        return getAmount();
    }
    public static boolean upgrade(MiningGameGUI display) {
        if(level < 4 && getUpgradePrice() <= display.getAvailableMoney()) {
            display.spendMoney(getUpgradePrice());
            level++;
            if (level == 4)
            {
                isGoldPickaxe = true;
            }
            return true;
        }
        return false;
    }
    public int getLevel() {
        return level;
    }
    public void resetLevel() {
        level = 1;
        isGoldPickaxe = false;
    }
}