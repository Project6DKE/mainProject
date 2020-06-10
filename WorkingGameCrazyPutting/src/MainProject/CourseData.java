package MainProject;

public class CourseData {
    public static double mass;

    public static double friction;

    public static double holeDist;

    public static int startX;
    public static int startY;

    public static String fileName;

    public static double ballSpeed;

    public static int goalX;
    public static int goalY;

    public static String function;

    public static double gravity;

    private static Object[] getDataList() {
        return new Object[]{mass, friction, holeDist, startX, startY,
                fileName, ballSpeed, goalX, goalY, function, gravity};
    }

    public static void printAllData() {
        for (Object element: getDataList()) {
            System.out.println(element);
        }
    }
}
