import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class DisclosureAttack {
    private ArrayList<String> partners;
    private Random rand;
    private int communicationPartners;
    private int totalUsers;
    private int nbrSenders;
    private int[][] R;
    private int[][] Rreturn;


    public DisclosureAttack(int nbrSenders, int communicationPartners, int totalUsers) {
        this.nbrSenders = nbrSenders;
        this.communicationPartners = communicationPartners;
        this.totalUsers = totalUsers;
        partners = new ArrayList<>();
        R = new int[communicationPartners][totalUsers];
        Rreturn = new int[communicationPartners][totalUsers];
        rand = new Random();

        fillRecievers(); //Add what recievers every sender usually communicates with.
        catchMatrices();
        System.out.println("left catchMatrices");

        boolean oneInEach = false;
        int nbrOfFoundR;
        int[] newR;
        while (!oneInEach) {
            newR = generateNewR();
            if (checkRequirements(newR)) {
                System.out.println("checkrequirements was true ");
                for (int i = 0; i < communicationPartners; i++) {
                    nbrOfFoundR = 0;
                    if (IntStream.of(R[i]).sum() == 1) {
                        System.out.println("Sum is one in nbrR " + nbrOfFoundR);
                        nbrOfFoundR++;
                    }
                    if (nbrOfFoundR == communicationPartners) {
                        oneInEach = true;
                    }
                }
            }
        }

        System.out.println(recieveCommPartners());


    }


    private String recieveCommPartners() {
        String partners = "";
        for (int j = 0; j < communicationPartners; j++) {
            for (int i = 0; i < totalUsers; i++) {
                if (Rreturn[j][i] == 1) {
                    partners = partners + Integer.toString(i) + ";";
                }
            }
        }
        return partners;
    }

    private void fillRecievers() {
        for (int i = 0; i < nbrSenders; i++) {
            String recievers = "";
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


    private void catchMatrices() {
        int[] testVector;
        int nbrOfR = 0;
        while (nbrOfR < communicationPartners) {              //until 5 disjointed matrixes are found.
            testVector = new int[totalUsers];
            for (int j = 0; j < nbrSenders; j++) {      //one sender from each user
                String recieve = partners.get(j);
                int temp = rand.nextInt(communicationPartners);
                String[] tmp = recieve.split(";");
                recieve = tmp[temp];
                testVector[Integer.valueOf(recieve)] = 1;   //set 'have recieved'
            }
            if (checkIfDisjoint(testVector)) { //check if testvector is disjoint with re
                nbrOfR++;
                System.out.println("Disjoint R found " + nbrOfR);
            }
        }
        System.out.println("Leaving?");
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


    private boolean checkIfDisjoint(int[] firstR, int[] secondR) {
        for (int i = 0; i < firstR.length; i++) {
            if (firstR[i] == 1 && secondR[i] == 1) {
                return false;
            }
        }
        return true;
    }


    private int[] generateNewR() {
        int[] returnVector = new int[totalUsers];
        String recieve;

        for (int j = 0; j < nbrSenders; j++) {
            recieve = partners.get(j);
            int temp = rand.nextInt(communicationPartners);
            String[] tmp = recieve.split(";");
            recieve = tmp[temp];
            returnVector[Integer.valueOf(recieve)] = 1;   //set 'sent'
        }

        return returnVector;
    }


    private boolean checkRequirements(int[] vector) {
        int track = 0;
        int which = 0;

        for (int i = 0; i < communicationPartners; i++) {
            if (!checkIfDisjoint(vector, R[i])) {
                track++;
                which = i;
            }
        }
        if (track == 1) {
            for (int i = 0; i < R[0].length; i++) {
                if (R[which][i] == 1 && vector[i] != 1) {
                    R[which][i] = 0;
                }
            }
            return true;
        }
        return false;
    }
}
