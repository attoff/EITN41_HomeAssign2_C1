import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class DisclosureAttack {
    private String partners;
    private Random rand;
    private int communicationPartners;
    private int totalUsers;
    private int nbrSenders;
    private int[][] R;


    public DisclosureAttack(int nbrSenders, int communicationPartners, int totalUsers) {
        this.nbrSenders = nbrSenders;
        this.communicationPartners = communicationPartners;
        this.totalUsers = totalUsers;
        R = new int[communicationPartners][totalUsers];
        rand = new Random();

        partners = fillAliceRecievers();
        catchMatrices();
        System.out.println("left catchMatrices");

        boolean oneInEach = false;
        int nbrOfFoundR;
        int[] newR;
        while (!oneInEach) {
            newR = generateNewR();
            if (checkRequirements(newR)) {
                nbrOfFoundR = 0;
                for (int i = 0; i < communicationPartners; i++) {
                    if (IntStream.of(R[i]).sum() == 1) {
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
                if (R[j][i] == 1) {
                    partners = partners + Integer.toString(i) + ";";
                }
            }
        }
        return partners;
    }

    private String fillAliceRecievers() {
        String recievers = "";
        int temp;
        for (int j = 0; j < communicationPartners; j++) {
            temp = rand.nextInt(totalUsers);
            while (recievers.contains(Integer.toString(temp))) {
                temp = rand.nextInt(totalUsers);
            }

            recievers = recievers + temp + ";";
        }
        System.out.println("Alice is talking to " + recievers);
        return recievers;

    }


    private void catchMatrices() {
        int[] testVector;
        int nbrOfR = 0;
        int temp;
        while (nbrOfR < communicationPartners) {

            testVector = new int[totalUsers];
            String recieve = partners;
            temp = rand.nextInt(communicationPartners);
            String[] tmp = recieve.split(";");
            recieve = tmp[temp];
            testVector[Integer.valueOf(recieve)] = 1;

            for (int j = 1; j < nbrSenders; j++) {
                temp = rand.nextInt(totalUsers);
                while (temp == j) {
                    temp = rand.nextInt(totalUsers);
                }
                testVector[temp] = 1;
            }


            if (checkIfDisjoint(testVector)) {
                for (int i = 0; i < R[0].length; i++) {
                    R[nbrOfR][i] = testVector[i];
                }
                System.out.println("Disjoint R found " + nbrOfR);
                nbrOfR++;
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

        recieve = partners;
        int temp = rand.nextInt(communicationPartners);
        String[] tmp = recieve.split(";");
        recieve = tmp[temp];
        returnVector[Integer.valueOf(recieve)] = 1;
        for (int j = 1; j < nbrSenders; j++) {
            temp = rand.nextInt(totalUsers);
            while (temp == j) {
                temp = rand.nextInt(totalUsers);
            }
            returnVector[temp] = 1;
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
