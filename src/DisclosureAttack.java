import java.util.ArrayList;
import java.util.Random;

/**
 * Created by vikto on 2016-11-24.
 */
public class DisclosureAttack {
    private ArrayList<String> partners;
    private int communicationPartners;
    private int totalUsers;
    private int nbrSenders;
    private int[][] R;


    public DisclosureAttack(int nbrSenders, int communicationPartners, int totalUsers) {
        this.communicationPartners = communicationPartners;
        this.totalUsers = totalUsers;
        this.nbrSenders = nbrSenders;
        partners = new ArrayList<>();
        R = new int[communicationPartners][totalUsers];
        fillRecievers();
        sendMessageAndFillR();

        int numberOfSets = 0;
        boolean disjoint = false;
        boolean nondisjoint = false;
        while (numberOfSets < communicationPartners) {
            int[] newR = generateR();
            int save = Integer.MIN_VALUE;
            for (int i = 0; i < R.length; i++) {
                if (checkIfDisjoin(newR, R[i])) {
                    disjoint = true;
                    break;
                }
            }
            for (int i = 0; i < R.length; i++) {
                if (!checkIfDisjoin(newR, R[i])) {
                    nondisjoint = true;
                    save = i;
                    break;
                }
            }

            int amountOfItems = 0;
            if (disjoint && nondisjoint) {
                for (int i = 0; i < R[0].length; i++) {
                    amountOfItems = 0;
                    if (R[save][i] == 1 && newR[i] != 1) {
                        R[save][i] = 0;
                    }
                    if (R[save][i] == 1 && newR[i] == 1) {
                        amountOfItems++;
                    }
                }
            }
            if (amountOfItems == 1) {
                numberOfSets++;
            }

        }

    }

    public String recieveCommPartners() {
        String partners = "";
        for (int j = 0; j < communicationPartners; j++) {
            for (int i = 0; i < totalUsers; i++) {
                if (R[j][i] == 1) {
                    partners = partners + Integer.toString(i) + ";";
                }
            }
        }
        return partners;
    }

    private int[] generateR() {
        int[] R = new int[totalUsers];
        Random rand = new Random();
        for (int j = 0; j < nbrSenders; j++) {
            String recieve = partners.get(j);
            int temp = rand.nextInt(communicationPartners);
            String[] tmp = recieve.split(";");
            recieve = tmp[temp];
            R[Integer.valueOf(recieve)] = 1;
        }
        return R;

    }

    private void fillRecievers() {
        Random rand = new Random();
        for (int i = 0; i < nbrSenders; i++) {
            String recievers = new String();
            int temp;
            for (int j = 0; j < communicationPartners; j++) {
                temp = rand.nextInt(totalUsers);
                while (recievers.contains(Integer.toString(temp)) || temp == i) {
                    temp = rand.nextInt(totalUsers);
                }

                recievers = recievers + temp + ";";
            }
            partners.add(recievers);
        }
    }


    private void sendMessageAndFillR() {
        Random rand = new Random();
        int temp;
        boolean goForward = false;

        for (int i = 0; i < communicationPartners; i++) {
            int[] vector = new int[totalUsers];

            while (!goForward) {
                vector = new int[totalUsers];
                for (int j = 0; j < nbrSenders; j++) {
                    String recieve = partners.get(j);
                    temp = rand.nextInt(communicationPartners);
                    String[] tmp = recieve.split(";");
                    recieve = tmp[temp];
                    vector[Integer.valueOf(recieve)] = 1;
                }
                if (checkIfDisjoint(vector)) {
                    goForward = true;
                    System.out.println("BAM!");
                }
            }
            R[i] = vector;
            goForward = false;
        }
    }


    private boolean checkIfDisjoint(int[] recievers) {
        for (int j = 0; j < communicationPartners; j++) {
            for (int i = 0; i < totalUsers; i++) {
                if (recievers[i] == 1 && R[j][i] == 1) {
                    return false;
                }

            }
        }
        return true;
    }

    private boolean checkIfDisjoin(int[] firstR, int[] secondR) {
        for (int i = 0; i < firstR.length; i++) {
            if (firstR[i] == 1 && secondR[i] == 1) {
                return false;
            }
        }
        return true;
    }

}
