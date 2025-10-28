package edu.sc.csce747.MeetingPlanner;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class CalendarTest {
	
	private Calendar calendar;
	
	@Before
	public void setUp() {
		calendar = new Calendar();
	}
	
	// ==================== ЗӨВШӨӨРӨГДӨХ ТОХИОЛДЛУУД ====================
	
	@Test
	public void testAddMeeting_validMeeting() {
		try {
			Meeting meeting = new Meeting(3, 15, 10, 12);
			calendar.addMeeting(meeting);
			assertTrue("Meeting should be added successfully", 
					calendar.isBusy(3, 15, 10, 12));
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	@Test
	public void testAddMeeting_holiday() {
		try {
			Meeting midsommar = new Meeting(6, 26, "Midsommar");
			calendar.addMeeting(midsommar);
			Boolean added = calendar.isBusy(6, 26, 0, 23);
			assertTrue("Midsommar should be marked as busy on the calendar", added);
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	@Test
	public void testAddMeeting_earlyMorning() {
		try {
			Meeting meeting = new Meeting(1, 5, 0, 3);
			calendar.addMeeting(meeting);
			assertTrue("Early morning meeting should be added", 
					calendar.isBusy(1, 5, 0, 3));
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	@Test
	public void testAddMeeting_lateNight() {
		try {
			Meeting meeting = new Meeting(7, 20, 20, 23);
			calendar.addMeeting(meeting);
			assertTrue("Late night meeting should be added", 
					calendar.isBusy(7, 20, 20, 23));
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	@Test
	public void testAddMeeting_multipleMeetingsSameDay() {
		try {
			Meeting morning = new Meeting(5, 10, 8, 10);
			Meeting afternoon = new Meeting(5, 10, 14, 16);
			Meeting evening = new Meeting(5, 10, 18, 20);
			
			calendar.addMeeting(morning);
			calendar.addMeeting(afternoon);
			calendar.addMeeting(evening);
			
			assertTrue("Morning meeting should be busy", calendar.isBusy(5, 10, 8, 10));
			assertTrue("Afternoon meeting should be busy", calendar.isBusy(5, 10, 14, 16));
			assertTrue("Evening meeting should be busy", calendar.isBusy(5, 10, 18, 20));
			assertFalse("Time between meetings should be free", calendar.isBusy(5, 10, 11, 13));
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	@Test
	public void testIsBusy_whenFree() {
		try {
			assertFalse("Empty calendar should not be busy", 
					calendar.isBusy(3, 15, 10, 12));
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	@Test
	public void testIsBusy_whenBusy() {
		try {
			Meeting meeting = new Meeting(4, 20, 13, 15);
			calendar.addMeeting(meeting);
			assertTrue("Should be busy during meeting time", 
					calendar.isBusy(4, 20, 13, 15));
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	@Test
	public void testClearSchedule() {
		try {
			Meeting m1 = new Meeting(8, 5, 9, 10);
			Meeting m2 = new Meeting(8, 5, 14, 16);
			calendar.addMeeting(m1);
			calendar.addMeeting(m2);
			
			assertTrue("Should be busy before clear", calendar.isBusy(8, 5, 9, 10));
			calendar.clearSchedule(8, 5);
			assertFalse("Should be free after clear", calendar.isBusy(8, 5, 9, 10));
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	@Test
	public void testPrintAgenda_month() {
		try {
			Meeting m = new Meeting(3, 10, 10, 12);
			calendar.addMeeting(m);
			String agenda = calendar.printAgenda(3);
			assertNotNull("Agenda should not be null", agenda);
			assertTrue("Agenda should contain month info", agenda.contains("Agenda for 3"));
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	@Test
	public void testPrintAgenda_day() {
		try {
			Meeting m = new Meeting(5, 15, 14, 16);
			calendar.addMeeting(m);
			String agenda = calendar.printAgenda(5, 15);
			assertNotNull("Agenda should not be null", agenda);
			assertTrue("Agenda should contain day info", agenda.contains("5/15"));
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	// ==================== БУРУУ ОРОЛТ - САР ====================
	
	@Test
	public void testAddMeeting_invalidMonth_zero() {
		Meeting meeting = new Meeting(0, 15, 10, 12);
		try {
			calendar.addMeeting(meeting);
			fail("Should throw TimeConflictException for month 0");
		} catch(TimeConflictException e) {
			assertTrue("Exception message should mention month", 
					e.getMessage().contains("Month"));
		}
	}
	
	@Test
	public void testAddMeeting_invalidMonth_thirteen() {
		Meeting meeting = new Meeting(13, 15, 10, 12);
		try {
			calendar.addMeeting(meeting);
			fail("Should throw TimeConflictException for month 13");
		} catch(TimeConflictException e) {
			assertTrue("Exception message should mention month", 
					e.getMessage().contains("Month"));
		}
	}
	
	@Test
	public void testAddMeeting_invalidMonth_negative() {
		Meeting meeting = new Meeting(-1, 15, 10, 12);
		try {
			calendar.addMeeting(meeting);
			fail("Should throw TimeConflictException for negative month");
		} catch(TimeConflictException e) {
			assertTrue("Exception message should mention month", 
					e.getMessage().contains("Month"));
		}
	}
	
	// ЭНЭ ТЕСТ НЬ 12 САРЫН АЛДААГ ИЛРҮҮЛНЭ!
	@Test
	public void testAddMeeting_validMonth_december() {
		Meeting meeting = new Meeting(12, 15, 10, 12);
		try {
			calendar.addMeeting(meeting);
			assertTrue("December (month 12) should be valid", 
					calendar.isBusy(12, 15, 10, 12));
		} catch(TimeConflictException e) {
			fail("December should be a valid month, but got exception: " + e.getMessage());
		}
	}
	
	// ==================== БУРУУ ОРОЛТ - ӨДӨР ====================
	
	@Test
	public void testAddMeeting_invalidDay_zero() {
		Meeting meeting = new Meeting(5, 0, 10, 12);
		try {
			calendar.addMeeting(meeting);
			fail("Should throw TimeConflictException for day 0");
		} catch(TimeConflictException e) {
			assertTrue("Exception message should mention day", 
					e.getMessage().contains("Day"));
		}
	}
	
	@Test
	public void testAddMeeting_invalidDay_thirtytwo() {
		Meeting meeting = new Meeting(5, 32, 10, 12);
		try {
			calendar.addMeeting(meeting);
			fail("Should throw TimeConflictException for day 32");
		} catch(TimeConflictException e) {
			assertTrue("Exception message should mention day", 
					e.getMessage().contains("Day"));
		}
	}
	
	@Test
	public void testAddMeeting_invalidDay_negative() {
		Meeting meeting = new Meeting(5, -5, 10, 12);
		try {
			calendar.addMeeting(meeting);
			fail("Should throw TimeConflictException for negative day");
		} catch(TimeConflictException e) {
			assertTrue("Exception message should mention day", 
					e.getMessage().contains("Day"));
		}
	}
	
	// ==================== ОРШИН БАЙХГҮЙ ӨДРҮҮД ====================
	
	@Test
	public void testAddMeeting_february29() {
		Meeting meeting = new Meeting(2, 29, 10, 12);
		try {
			calendar.addMeeting(meeting);
			fail("February 29 should not exist in this calendar");
		} catch(TimeConflictException e) {
			assertTrue("Exception should indicate overlap with 'Day does not exist'", 
					e.getMessage().contains("Day does not exist"));
		}
	}
	
	@Test
	public void testAddMeeting_february30() {
		Meeting meeting = new Meeting(2, 30, 10, 12);
		try {
			calendar.addMeeting(meeting);
			fail("February 30 does not exist");
		} catch(TimeConflictException e) {
			assertTrue("Exception should indicate day does not exist", 
					e.getMessage().contains("Day does not exist"));
		}
	}
	
	@Test
	public void testAddMeeting_february31() {
		Meeting meeting = new Meeting(2, 31, 10, 12);
		try {
			calendar.addMeeting(meeting);
			fail("February 31 does not exist");
		} catch(TimeConflictException e) {
			assertTrue("Exception should indicate day does not exist", 
					e.getMessage().contains("Day does not exist"));
		}
	}
	
	@Test
	public void testAddMeeting_april31() {
		Meeting meeting = new Meeting(4, 31, 10, 12);
		try {
			calendar.addMeeting(meeting);
			fail("April 31 does not exist");
		} catch(TimeConflictException e) {
			assertTrue("Exception should indicate day does not exist", 
					e.getMessage().contains("Day does not exist"));
		}
	}
	
	@Test
	public void testAddMeeting_june31() {
		Meeting meeting = new Meeting(6, 31, 10, 12);
		try {
			calendar.addMeeting(meeting);
			fail("June 31 does not exist");
		} catch(TimeConflictException e) {
			assertTrue("Exception should indicate day does not exist", 
					e.getMessage().contains("Day does not exist"));
		}
	}
	
	@Test
	public void testAddMeeting_september31() {
		Meeting meeting = new Meeting(9, 31, 10, 12);
		try {
			calendar.addMeeting(meeting);
			fail("September 31 does not exist");
		} catch(TimeConflictException e) {
			assertTrue("Exception should indicate day does not exist", 
					e.getMessage().contains("Day does not exist"));
		}
	}
	
	// ЭНЭ ТЕСТ НЬ 11 САРЫН АЛДААГ ИЛРҮҮЛНЭ!
	@Test
	public void testAddMeeting_november30_shouldBeValid() {
		Meeting meeting = new Meeting(11, 30, 10, 12);
		try {
			calendar.addMeeting(meeting);
			assertTrue("November 30 should be a valid date", 
					calendar.isBusy(11, 30, 10, 12));
		} catch(TimeConflictException e) {
			fail("November 30 is a valid date, but got exception: " + e.getMessage());
		}
	}
	
	@Test
	public void testAddMeeting_november31() {
		Meeting meeting = new Meeting(11, 31, 10, 12);
		try {
			calendar.addMeeting(meeting);
			fail("November 31 does not exist");
		} catch(TimeConflictException e) {
			assertTrue("Exception should indicate day does not exist", 
					e.getMessage().contains("Day does not exist"));
		}
	}
	
	// ==================== БУРУУ ЦАГ ====================
	
	@Test
	public void testAddMeeting_invalidStartTime_negative() {
		Meeting meeting = new Meeting(5, 15, -1, 12);
		try {
			calendar.addMeeting(meeting);
			fail("Should throw TimeConflictException for negative start time");
		} catch(TimeConflictException e) {
			assertTrue("Exception message should mention illegal hour", 
					e.getMessage().contains("Illegal hour"));
		}
	}
	
	@Test
	public void testAddMeeting_invalidStartTime_twentyfour() {
		Meeting meeting = new Meeting(5, 15, 24, 25);
		try {
			calendar.addMeeting(meeting);
			fail("Should throw TimeConflictException for start time 24");
		} catch(TimeConflictException e) {
			assertTrue("Exception message should mention illegal hour", 
					e.getMessage().contains("Illegal hour"));
		}
	}
	
	@Test
	public void testAddMeeting_invalidEndTime_negative() {
		Meeting meeting = new Meeting(5, 15, 10, -1);
		try {
			calendar.addMeeting(meeting);
			fail("Should throw TimeConflictException for negative end time");
		} catch(TimeConflictException e) {
			assertTrue("Exception message should mention illegal hour", 
					e.getMessage().contains("Illegal hour"));
		}
	}
	
	@Test
	public void testAddMeeting_invalidEndTime_twentyfour() {
		Meeting meeting = new Meeting(5, 15, 10, 24);
		try {
			calendar.addMeeting(meeting);
			fail("Should throw TimeConflictException for end time 24");
		} catch(TimeConflictException e) {
			assertTrue("Exception message should mention illegal hour", 
					e.getMessage().contains("Illegal hour"));
		}
	}
	
	@Test
	public void testAddMeeting_startEqualsEnd() {
		Meeting meeting = new Meeting(5, 15, 10, 10);
		try {
			calendar.addMeeting(meeting);
			fail("Should throw TimeConflictException when start equals end");
		} catch(TimeConflictException e) {
			assertTrue("Exception should mention meeting timing issue", 
					e.getMessage().contains("starts before it ends"));
		}
	}
	
	@Test
	public void testAddMeeting_startAfterEnd() {
		Meeting meeting = new Meeting(5, 15, 15, 10);
		try {
			calendar.addMeeting(meeting);
			fail("Should throw TimeConflictException when start > end");
		} catch(TimeConflictException e) {
			assertTrue("Exception should mention meeting timing issue", 
					e.getMessage().contains("starts before it ends"));
		}
	}
	
	// ==================== ДАВХЦАЛ ====================
	
	@Test
	public void testAddMeeting_exactOverlap() {
		try {
			Meeting m1 = new Meeting(7, 10, 10, 12);
			Meeting m2 = new Meeting(7, 10, 10, 12);
			
			calendar.addMeeting(m1);
			calendar.addMeeting(m2);
			fail("Should throw TimeConflictException for exact overlap");
		} catch(TimeConflictException e) {
			assertTrue("Exception should mention overlap", 
					e.getMessage().contains("Overlap"));
		}
	}
	
	@Test
	public void testAddMeeting_startOverlap() {
		try {
			Meeting m1 = new Meeting(7, 10, 10, 14);
			Meeting m2 = new Meeting(7, 10, 12, 16);
			
			calendar.addMeeting(m1);
			calendar.addMeeting(m2);
			fail("Should throw TimeConflictException for start time overlap");
		} catch(TimeConflictException e) {
			assertTrue("Exception should mention overlap", 
					e.getMessage().contains("Overlap"));
		}
	}
	
	@Test
	public void testAddMeeting_endOverlap() {
		try {
			Meeting m1 = new Meeting(7, 10, 14, 18);
			Meeting m2 = new Meeting(7, 10, 10, 16);
			
			calendar.addMeeting(m1);
			calendar.addMeeting(m2);
			fail("Should throw TimeConflictException for end time overlap");
		} catch(TimeConflictException e) {
			assertTrue("Exception should mention overlap", 
					e.getMessage().contains("Overlap"));
		}
	}
	
	@Test
	public void testAddMeeting_containedInside() {
		try {
			Meeting m1 = new Meeting(7, 10, 9, 17);
			Meeting m2 = new Meeting(7, 10, 12, 14);
			
			calendar.addMeeting(m1);
			calendar.addMeeting(m2);
			fail("Should throw TimeConflictException when meeting is contained inside another");
		} catch(TimeConflictException e) {
			assertTrue("Exception should mention overlap", 
					e.getMessage().contains("Overlap"));
		}
	}
	
	@Test
	public void testAddMeeting_containsAnother() {
		try {
			Meeting m1 = new Meeting(7, 10, 12, 14);
			Meeting m2 = new Meeting(7, 10, 9, 17);
			
			calendar.addMeeting(m1);
			calendar.addMeeting(m2);
			fail("Should throw TimeConflictException when meeting contains another");
		} catch(TimeConflictException e) {
			assertTrue("Exception should mention overlap", 
					e.getMessage().contains("Overlap"));
		}
	}
	
	@Test
	public void testAddMeeting_adjacentMeetings_shouldSucceed() {
		try {
			Meeting m1 = new Meeting(7, 10, 10, 12);
			Meeting m2 = new Meeting(7, 10, 13, 15);
			
			calendar.addMeeting(m1);
			calendar.addMeeting(m2);
			
			assertTrue("First meeting should be busy", calendar.isBusy(7, 10, 10, 12));
			assertTrue("Second meeting should be busy", calendar.isBusy(7, 10, 13, 15));
		} catch(TimeConflictException e) {
			fail("Adjacent meetings should not conflict: " + e.getMessage());
		}
	}
	
	// ==================== GETMEETING ба REMOVEMEETING ====================
	
	@Test
	public void testGetMeeting() {
		try {
			Meeting m = new Meeting(4, 12, 10, 11);
			calendar.addMeeting(m);
			Meeting retrieved = calendar.getMeeting(4, 12, 0);
			assertNotNull("Retrieved meeting should not be null", retrieved);
			assertEquals("Month should match", 4, retrieved.getMonth());
			assertEquals("Day should match", 12, retrieved.getDay());
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	@Test
	public void testRemoveMeeting() {
		try {
			Meeting m = new Meeting(4, 12, 10, 11);
			calendar.addMeeting(m);
			assertTrue("Should be busy before removal", calendar.isBusy(4, 12, 10, 11));
			
			calendar.removeMeeting(4, 12, 0);
			assertFalse("Should be free after removal", calendar.isBusy(4, 12, 10, 11));
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
}