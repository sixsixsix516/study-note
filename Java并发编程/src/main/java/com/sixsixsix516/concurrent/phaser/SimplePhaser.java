package com.sixsixsix516.concurrent.phaser;

import java.util.concurrent.Phaser;

/**
 * @author sun 2020/9/16 11:06
 */
public class SimplePhaser extends Phaser {


	@Override
	protected boolean onAdvance(int phase, int registeredParties) {    //在每个阶段执行完成后回调的方法
		System.out.println("回调的阶段是: " + phase);
		switch (phase) {
			case 0:
				return studentArrived();
			case 1:
				return finishFirstExercise();
			case 2:
				return finishSecondExercise();
			case 3:
				return finishExam();
			default:
				return true;
		}
	}

	private boolean studentArrived() {
		System.out.println("学生准备好了,学生人数：" + getRegisteredParties());
		return false;
	}

	private boolean finishFirstExercise() {
		System.out.println("第一题所有学生做完");
		return false;
	}

	private boolean finishSecondExercise() {
		System.out.println("第二题所有学生做完");
		return false;
	}

	private boolean finishExam() {
		System.out.println("第三题所有学生做完，结束考试");
		return true;
	}

}
