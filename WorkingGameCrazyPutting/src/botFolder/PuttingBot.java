package botFolder;

import MainProject.PuttingCourse;
import MainProject.Vector2d;

public interface PuttingBot {
	public Vector2d shot_velocity(PuttingCourse course, Vector2d ball_position);
}
