package com.snd.app.data;

// 작업의 속도를 늦추기 위한 백그라운드 스레드
class SlowTask implements Runnable {
    @Override
    public void run() {
        try {
            // Sleep for a while to simulate an expensive operation
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
