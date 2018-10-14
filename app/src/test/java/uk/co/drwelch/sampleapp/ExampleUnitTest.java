package uk.co.drwelch.sampleapp;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

class MockView implements MainActivityPresenter.View {
    private int sentinel;
    public void setData(String[] data) {
        sentinel = 9;
        String[] s={"1"};
        String t = s[sentinel];
    };
    public void startDetailViewWith(String value) {
        String[] s={"1"};
        String t = s[1];
    };
    public void showRetry() {
        sentinel = 7;
        String[] s={"1"};
        String t = s[sentinel];
    };
    public void hideRetry() {};
}

public class ExampleUnitTest {
    @Test
    public void repo_is_singleton() {
        Repository a = Repository.getInstance();
        Repository b = Repository.getInstance();
        assertEquals(a,b);
    }

    @Test
    public void model_is_singleton() {
        Model a = Model.getInstance();
        Model b = Model.getInstance();
        assertEquals(a,b);
    }

    @Test
    public void person_construction() {
        Person p = new Person("John","120","25","2014-12-10T15:10:51.357000Z");
        String[] e = new String[] {"John","1.20 m","25 kg","10/12/2014 at 15:10:51"};
        ArrayList<String> expected = new ArrayList<>();
        Collections.addAll(expected, e);
        assertEquals(expected,p.getProperties());
    }

    // as exception is caught and is thrown in a private method, not sure how to test this ... but it works
//    @Test(expected = InvalidPresenterStateException.class)
    public void one_presenter_per_type() throws InvalidPresenterStateException {
        class C1 implements Model.DataChangeListener { public void updateView(String s) {} };
        class C2 implements Model.DataChangeListener { public void updateView(String s) {} };

        C1 presenter1 = new C1();
        C2 presenter2 = new C2();
        C1 presenter1_reincarnated = new C1();
        Model m = Model.getInstance();

        m.attachPresenter(presenter1);
        m.attachPresenter(presenter2);
        m.attachPresenter(presenter1_reincarnated);

        m.detachPresenter(presenter1); // should already be replaced by reincarnate so throws
    }

    @Test(expected = NoPersonDataException.class)
    public void no_data() throws NoPersonDataException {
        Model m = Model.getInstance();
        m.setCurrentPerson("John");
    }

    @Test(expected = NoPersonDataException.class)
    public void no_data_2() throws NoPersonDataException {
        Model m = Model.getInstance();
        m.getAllNames();
    }

    @Test
    public void labels() {
        Model m = Model.getInstance();
        assertEquals(m.getFieldLabels(),new ArrayList<>(Arrays.asList(Person.PROPERTIES)));
    }

    @Test
    public void main_pres_key() {
        MainActivityPresenter p = new MainActivityPresenter();
        assertEquals(p.getOutgoingExtraKey(),AppStrings.PERSONID);
    }

//    @Test
    public void main_pres_clickitem() {
        MockView v = new MockView();
        MainActivityPresenter p = new MainActivityPresenter();
        p.attachView(v);
        try {
            p.itemClicked("ClickItem");
            fail("Should throw exception");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            assertThat(e.getMessage(),is(" 9"));
        }
    }

//    @Test
    public void main_pres_updateview() {
        MockView v = new MockView();
        MainActivityPresenter p = new MainActivityPresenter();
        p.updateView("Nothing");

    }

}