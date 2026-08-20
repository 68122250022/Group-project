import java.util.ArrayDeque;
import java.util.Deque;

public class WarehouseManager {
    private Deque<Box>[] stacks;
    private int capacityPerStack;

    // ตัวแปรนับจำนวน Operation
    private int pushCount = 0;
    private int popCount = 0;
    private int compareCount = 0;

    public WarehouseManager(int k, int capacity) {
        this.capacityPerStack = capacity;
        stacks = new ArrayDeque[k];
        for (int i = 0; i < k; i++) {
            stacks[i] = new ArrayDeque<>();
        }
    }

    // ฟังก์ชันช่วยใส่ข้อมูลจำลอง
    public void setupDummyData() {
        for (Deque<Box> stack : stacks) stack.clear();
        pushToStack(0, new Box("A")); // กล่องล่างสุด
        pushToStack(0, new Box("B")); // กล่องเป้าหมาย
        pushToStack(0, new Box("C"));
        pushToStack(0, new Box("D")); // กล่องบนสุด
        pushToStack(1, new Box("E")); // Stack 1 มีกล่อง 1 ใบ
        pushToStack(2, new Box("F")); // Stack 2 มีกล่อง 2 ใบ
        pushToStack(2, new Box("G"));
        resetCounters();
    }

    private void resetCounters() {
        pushCount = 0; popCount = 0; compareCount = 0;
    }

    private void pushToStack(int stackIndex, Box box) {
        stacks[stackIndex].push(box);
        pushCount++;
    }

    private Box popFromStack(int stackIndex) {
        Box b = stacks[stackIndex].pop();
        popCount++;
        return b;
    }

    // แสดงสถานะของ Stack (ข้อ 7)
    public void displayStacks() {
        System.out.println("\n=== สถานะ Stack ปัจจุบัน ===");
        for (int i = 0; i < stacks.length; i++) {
            int remaining = capacityPerStack - stacks[i].size();
            System.out.print("Stack " + (i + 1) + " (ว่าง " + remaining + " ช่อง): ");
            if (stacks[i].isEmpty()) {
                System.out.println("[ ว่าง ]");
            } else {
                // แปลงเป็นอาร์เรย์เพื่อปริ้นท์จากล่างขึ้นบน
                Object[] arr = stacks[i].toArray();
                for (int j = arr.length - 1; j >= 0; j--) {
                    System.out.print(arr[j] + " ");
                }
                System.out.println(" <- บนสุด(Top)");
            }
        }
    }

    private int findStackWithBox(String targetId) {
        for (int i = 0; i < stacks.length; i++) {
            for (Box b : stacks[i]) {
                if (b.getId().equals(targetId)) return i;
            }
        }
        return -1;
    }

    // ============================================
    // Algorithm A: First Available
    // ============================================
    public boolean retrieveFirstAvailable(String targetId) {
        resetCounters();
        int sourceIndex = findStackWithBox(targetId);
        if (sourceIndex == -1) {
            System.out.println("ไม่พบกล่องเป้าหมาย!");
            return false;
        }

        Deque<Integer> moveHistory = new ArrayDeque<>();

        while (!stacks[sourceIndex].isEmpty() && !stacks[sourceIndex].peek().getId().equals(targetId)) {
            Box boxToMove = popFromStack(sourceIndex);
            int destIndex = -1;

            // สแกนหา Stack แรกที่ว่าง
            for (int i = 0; i < stacks.length; i++) {
                if (i != sourceIndex) {
                    compareCount++;
                    if (stacks[i].size() < capacityPerStack) {
                        destIndex = i;
                        break; // เจอแล้วหยุดเลย
                    }
                }
            }

            if (destIndex == -1) {
                System.out.println("ไม่มีพื้นที่ว่างพอสำหรับย้ายกล่อง!");
                restoreBoxes(sourceIndex, moveHistory);
                return false;
            }
            pushToStack(destIndex, boxToMove);
            moveHistory.push(destIndex);
        }

        Box targetBox = popFromStack(sourceIndex);
        System.out.println("\nดึงเป้าหมาย " + targetBox.toString() + " สำเร็จ!");
        restoreBoxes(sourceIndex, moveHistory);

        System.out.println("จำนวน Operation: Push=" + pushCount + ", Pop=" + popCount + ", Compare=" + compareCount);
        return true;
    }

    // ============================================
    // Algorithm B: Best Fit
    // ============================================
    public boolean retrieveBestFit(String targetId) {
        resetCounters();
        int sourceIndex = findStackWithBox(targetId);
        if (sourceIndex == -1) {
            System.out.println("ไม่พบกล่องเป้าหมาย!");
            return false;
        }

        Deque<Integer> moveHistory = new ArrayDeque<>();

        while (!stacks[sourceIndex].isEmpty() && !stacks[sourceIndex].peek().getId().equals(targetId)) {
            Box boxToMove = popFromStack(sourceIndex);
            int destIndex = -1;
            int minSpace = Integer.MAX_VALUE;

            // สแกนทุก Stack หาที่ว่างที่น้อยที่สุด
            for (int i = 0; i < stacks.length; i++) {
                if (i != sourceIndex) {
                    compareCount++;
                    int space = capacityPerStack - stacks[i].size();
                    if (space > 0 && space < minSpace) {
                        minSpace = space;
                        destIndex = i;
                    }
                }
            }

            if (destIndex == -1) {
                System.out.println("ไม่มีพื้นที่ว่างพอสำหรับย้ายกล่อง!");
                restoreBoxes(sourceIndex, moveHistory);
                return false;
            }
            pushToStack(destIndex, boxToMove);
            moveHistory.push(destIndex);
        }

        Box targetBox = popFromStack(sourceIndex);
        System.out.println("\nดึงเป้าหมาย " + targetBox.toString() + " สำเร็จ!");
        restoreBoxes(sourceIndex, moveHistory);

        System.out.println("จำนวน Operation: Push=" + pushCount + ", Pop=" + popCount + ", Compare=" + compareCount);
        return true;
    }

    // ฟังก์ชันคืนกล่องกลับที่เดิม
    private void restoreBoxes(int sourceIndex, Deque<Integer> moveHistory) {
        while (!moveHistory.isEmpty()) {
            int destIndex = moveHistory.pop();
            Box b = popFromStack(destIndex);
            pushToStack(sourceIndex, b);
        }
    }
}