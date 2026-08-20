import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // สร้างโกดังจำลอง: มี 3 Stack, แต่ละ Stack รับได้ 5 กล่อง
        WarehouseManager manager = new WarehouseManager(3, 5);
        manager.setupDummyData();

        while (true) {
            System.out.println("\n=== เมนูจัดการคลังสินค้า ===");
            System.out.println("1. แสดงสถานะ Stack");
            System.out.println("2. ดึงกล่อง (Algorithm A - First Available)");
            System.out.println("3. ดึงกล่อง (Algorithm B - Best Fit)");
            System.out.println("4. รีเซ็ตข้อมูลจำลองใหม่");
            System.out.println("0. ออกจากโปรแกรม");
            System.out.print("เลือกคำสั่ง: ");

            // ดักจับ Error พิมพ์ผิด (Input Validation)
            if (!scanner.hasNextInt()) {
                System.out.println("กรุณากรอกเป็นตัวเลขเท่านั้น!");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();

            if (choice == 0) {
                System.out.println("จบการทำงาน");
                break;
            } else if (choice == 1) {
                manager.displayStacks();
            } else if (choice == 4) {
                manager.setupDummyData();
                System.out.println("รีเซ็ตข้อมูลสำเร็จ!");
            } else if (choice == 2 || choice == 3) {
                System.out.print("กรอก ID กล่องที่ต้องการดึง (เช่น B): ");
                String targetId = scanner.next();

                // จับเวลา
                long startTime = System.nanoTime();
                boolean success = false;

                if (choice == 2) {
                    success = manager.retrieveFirstAvailable(targetId);
                } else if (choice == 3) {
                    success = manager.retrieveBestFit(targetId);
                }

                long endTime = System.nanoTime();

                if (success) {
                    System.out.println("ใช้เวลาทำงานทั้งหมด: " + (endTime - startTime) + " ns");
                }
            } else {
                System.out.println("ไม่มีคำสั่งนี้!");
            }
        }
        scanner.close();
    }
}