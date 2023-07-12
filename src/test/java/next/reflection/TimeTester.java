package next.reflection;

public class TimeTester {
    @ElapsedTime
    public void timer(){
        for(int i=0;i<1000000;i++){

        }
    }

    public void timerV2(){
        for(int i=0;i<10;i++){

        }
    }
}
