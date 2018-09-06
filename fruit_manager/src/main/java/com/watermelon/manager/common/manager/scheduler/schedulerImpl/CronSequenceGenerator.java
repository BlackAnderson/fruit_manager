package com.watermelon.manager.common.manager.scheduler.schedulerImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import org.springframework.util.StringUtils;

public class CronSequenceGenerator {
	private final BitSet seconds = new BitSet(60);
	private final BitSet minutes = new BitSet(60);
	private final BitSet hours = new BitSet(24);
	private final BitSet daysOfWeek = new BitSet(7);
	private final BitSet daysOfMonth = new BitSet(31);
	private final BitSet months = new BitSet(12);
	private final String expression;
	private final TimeZone timeZone;

	public CronSequenceGenerator(String expression, TimeZone timeZone) {
		this.expression = expression;
		this.timeZone = timeZone;
		this.parse(expression);
	}

	public Date next(Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeZone(this.timeZone);
		calendar.setTime(date);
		calendar.add(13, 1);
		calendar.set(14, 0);
		this.doNext(calendar, calendar.get(1));
		return calendar.getTime();
	}

	private void doNext(Calendar calendar, int dot) {
		ArrayList resets = new ArrayList();
		int second = calendar.get(13);
		List emptyList = Collections.emptyList();
		int updateSecond = this.findNext(this.seconds, second, calendar, 13, 12, emptyList);
		if (second == updateSecond) {
			resets.add(Integer.valueOf(13));
		}

		int minute = calendar.get(12);
		int updateMinute = this.findNext(this.minutes, minute, calendar, 12, 11, resets);
		if (minute == updateMinute) {
			resets.add(Integer.valueOf(12));
		} else {
			this.doNext(calendar, dot);
		}

		int hour = calendar.get(11);
		int updateHour = this.findNext(this.hours, hour, calendar, 11, 7, resets);
		if (hour == updateHour) {
			resets.add(Integer.valueOf(11));
		} else {
			this.doNext(calendar, dot);
		}

		int dayOfWeek = calendar.get(7);
		int dayOfMonth = calendar.get(5);
		int updateDayOfMonth = this.findNextDay(calendar, this.daysOfMonth, dayOfMonth, this.daysOfWeek, dayOfWeek,
				resets);
		if (dayOfMonth == updateDayOfMonth) {
			resets.add(Integer.valueOf(5));
		} else {
			this.doNext(calendar, dot);
		}

		int month = calendar.get(2);
		int updateMonth = this.findNext(this.months, month, calendar, 2, 1, resets);
		if (month != updateMonth) {
			if (calendar.get(1) - dot > 4) {
				throw new IllegalStateException("Invalid cron expression led to runaway search for next trigger");
			}

			this.doNext(calendar, dot);
		}

	}

	private int findNextDay(Calendar calendar, BitSet daysOfMonth, int dayOfMonth, BitSet daysOfWeek, int dayOfWeek,
			List<Integer> resets) {
		int count = 0;
		short max = 366;

		while ((!daysOfMonth.get(dayOfMonth) || !daysOfWeek.get(dayOfWeek - 1)) && count++ < max) {
			calendar.add(5, 1);
			dayOfMonth = calendar.get(5);
			dayOfWeek = calendar.get(7);
			this.reset(calendar, resets);
		}

		if (count >= max) {
			throw new IllegalStateException("Overflow in day for expression=" + this.expression);
		} else {
			return dayOfMonth;
		}
	}

	private int findNext(BitSet bits, int value, Calendar calendar, int field, int nextField,
			List<Integer> lowerOrders) {
		int nextValue = bits.nextSetBit(value);
		if (nextValue == -1) {
			calendar.add(nextField, 1);
			this.reset(calendar, Arrays.asList(new Integer[]{Integer.valueOf(field)}));
			nextValue = bits.nextSetBit(0);
		}

		if (nextValue != value) {
			calendar.set(field, nextValue);
			this.reset(calendar, lowerOrders);
		}

		return nextValue;
	}

	private void reset(Calendar calendar, List<Integer> fields) {
		Iterator arg3 = fields.iterator();

		while (arg3.hasNext()) {
			int field = ((Integer) arg3.next()).intValue();
			calendar.set(field, field == 5 ? 1 : 0);
		}

	}

	private void parse(String expression) throws IllegalArgumentException {
		String[] fields = StringUtils.tokenizeToStringArray(expression, " ");
		if (fields.length != 6) {
			throw new IllegalArgumentException(
					String.format("cron expression must consist of 6 fields (found %d in %s)",
							new Object[]{Integer.valueOf(fields.length), expression}));
		} else {
			this.setNumberHits(this.seconds, fields[0], 0, 60);
			this.setNumberHits(this.minutes, fields[1], 0, 60);
			this.setNumberHits(this.hours, fields[2], 0, 24);
			this.setDaysOfMonth(this.daysOfMonth, fields[3]);
			this.setMonths(this.months, fields[4]);
			this.setDays(this.daysOfWeek, this.replaceOrdinals(fields[5], "SUN,MON,TUE,WED,THU,FRI,SAT"), 8);
			if (this.daysOfWeek.get(7)) {
				this.daysOfWeek.set(0);
				this.daysOfWeek.clear(7);
			}

		}
	}

	private String replaceOrdinals(String value, String commaSeparatedList) {
		String[] list = StringUtils.commaDelimitedListToStringArray(commaSeparatedList);

		for (int i = 0; i < list.length; ++i) {
			String item = list[i].toUpperCase();
			value = StringUtils.replace(value.toUpperCase(), item, "" + i);
		}

		return value;
	}

	private void setDaysOfMonth(BitSet bits, String field) {
		byte max = 31;
		this.setDays(bits, field, max + 1);
		bits.clear(0);
	}

	private void setDays(BitSet bits, String field, int max) {
		if (field.contains("?")) {
			field = "*";
		}

		this.setNumberHits(bits, field, 0, max);
	}

	private void setMonths(BitSet bits, String value) {
		byte max = 12;
		value = this.replaceOrdinals(value, "FOO,JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC");
		BitSet months = new BitSet(13);
		this.setNumberHits(months, value, 1, max + 1);

		for (int i = 1; i <= max; ++i) {
			if (months.get(i)) {
				bits.set(i - 1);
			}
		}

	}

	private void setNumberHits(BitSet bits, String value, int min, int max) {
		String[] fields = StringUtils.delimitedListToStringArray(value, ",");
		String[] arg8 = fields;
		int arg7 = fields.length;

		for (int arg6 = 0; arg6 < arg7; ++arg6) {
			String field = arg8[arg6];
			if (!field.contains("/")) {
				int[] arg13 = this.getRange(field, min, max);
				bits.set(arg13[0], arg13[1] + 1);
			} else {
				String[] split = StringUtils.delimitedListToStringArray(field, "/");
				if (split.length > 2) {
					throw new IllegalArgumentException("Incrementer has more than two fields: " + field);
				}

				int[] range = this.getRange(split[0], min, max);
				if (!split[0].contains("-")) {
					range[1] = max - 1;
				}

				int delta = Integer.valueOf(split[1]).intValue();

				for (int i = range[0]; i <= range[1]; i += delta) {
					bits.set(i);
				}
			}
		}

	}

	private int[] getRange(String field, int min, int max) {
		int[] result = new int[2];
		if (field.contains("*")) {
			result[0] = min;
			result[1] = max - 1;
			return result;
		} else {
			if (!field.contains("-")) {
				result[0] = result[1] = Integer.valueOf(field).intValue();
			} else {
				String[] split = StringUtils.delimitedListToStringArray(field, "-");
				if (split.length > 2) {
					throw new IllegalArgumentException("Range has more than two fields: " + field);
				}

				result[0] = Integer.valueOf(split[0]).intValue();
				result[1] = Integer.valueOf(split[1]).intValue();
			}

			if (result[0] < max && result[1] < max) {
				if (result[0] >= min && result[1] >= min) {
					return result;
				} else {
					throw new IllegalArgumentException("Range less than minimum (" + min + "): " + field);
				}
			} else {
				throw new IllegalArgumentException("Range exceeds maximum (" + max + "): " + field);
			}
		}
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof CronSequenceGenerator)) {
			return false;
		} else {
			CronSequenceGenerator cron = (CronSequenceGenerator) obj;
			return cron.months.equals(this.months) && cron.daysOfMonth.equals(this.daysOfMonth)
					&& cron.daysOfWeek.equals(this.daysOfWeek) && cron.hours.equals(this.hours)
					&& cron.minutes.equals(this.minutes) && cron.seconds.equals(this.seconds);
		}
	}

	public int hashCode() {
		return 37 + 17 * this.months.hashCode() + 29 * this.daysOfMonth.hashCode() + 37 * this.daysOfWeek.hashCode()
				+ 41 * this.hours.hashCode() + 53 * this.minutes.hashCode() + 61 * this.seconds.hashCode();
	}

	public String toString() {
		return this.getClass().getSimpleName() + ": " + this.expression;
	}
}