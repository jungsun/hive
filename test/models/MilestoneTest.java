package models;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class MilestoneTest extends ModelTest {

    @Test
    public void testCreate() {
        Milestone newMilestone = new Milestone();
        newMilestone.dueDate = new Date();
        newMilestone.contents = "테스트 마일스톤";
        newMilestone.numClosedIssues = 10;
        newMilestone.numOpenIssues = 20;
        newMilestone.projectId = 100l;
        newMilestone.versionName = "0.1";

        Milestone.create(newMilestone);

        assertThat(newMilestone.id, is(notNullValue()));
    }

    @Ignore
    public void testFindById() {
        Milestone firstMilestone = Milestone.findById(1l);
        assertThat(firstMilestone.versionName, is("v.0.1"));
        assertThat(firstMilestone.contents, is("nFORGE 첫번째 버전."));

        Calendar expactDueDate = new GregorianCalendar();
        expactDueDate.set(2012, 6, 12, 23, 59, 59); // 2012-07-12

        Calendar dueDate = new GregorianCalendar();
        dueDate.setTime(firstMilestone.dueDate);

        assertThat(expactDueDate.get(Calendar.YEAR), is(dueDate.get(Calendar.YEAR)));
        assertThat(expactDueDate.get(Calendar.MONTH), is(dueDate.get(Calendar.MONTH)));
        assertThat(expactDueDate.get(Calendar.DAY_OF_MONTH), is(dueDate.get(Calendar.DAY_OF_MONTH)));

        assertThat(firstMilestone.numClosedIssues, is(12));
        assertThat(firstMilestone.numOpenIssues, is(33));
        assertThat(firstMilestone.projectId, is(1l));
    }

    @Test
    public void testDelete() {
        Milestone firstMilestone = Milestone.findById(1l);
        assertThat(firstMilestone, is(notNullValue()));
        Milestone.delete(firstMilestone.id);

        firstMilestone = Milestone.findById(1l);
        assertThat(firstMilestone, is(nullValue()));
    }

    @Test
    public void testUpdate() {
        Milestone milestone = new Milestone();
        milestone.contents = "엔포지 첫번째 버전.";
        milestone.versionName = "1.0.0-SNAPSHOT";
        milestone.numClosedIssues = 100;
        milestone.numOpenIssues = 200;

        Milestone.update(milestone, 1l);

        Milestone actualMilestone = Milestone.findById(1l);
        assertThat(actualMilestone.contents, is(milestone.contents));
        assertThat(actualMilestone.versionName, is(milestone.versionName));
        assertThat(actualMilestone.numClosedIssues, is(100));
        assertThat(actualMilestone.numOpenIssues, is(200));
    }

    @Test
    public void testFindByProjectId() {
        List<Milestone> firstProjectMilestones = Milestone.findByProjectId(1l);
        assertThat(firstProjectMilestones.size(), is(2));

        checkIfTheMilestoneIsBelongToTheProject(firstProjectMilestones, 1l, 2l);

        List<Milestone> secondProjectMilestones = Milestone.findByProjectId(2l);
        assertThat(secondProjectMilestones.size(), is(2));

        checkIfTheMilestoneIsBelongToTheProject(secondProjectMilestones, 3l, 4l);
    }

    private void checkIfTheMilestoneIsBelongToTheProject(List<Milestone> milestones, Long... actualMilestoneIds) {
        List<Long> milestoneIds = Arrays.asList(actualMilestoneIds);
        for (Milestone milestone : milestones) {
            assertThat(milestoneIds.contains(milestone.id), is(true));
        }
    }
}