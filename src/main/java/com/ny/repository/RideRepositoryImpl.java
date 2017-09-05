package com.ny.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ny.model.Ride;
import com.ny.repository.util.RideRowMapper;

@Repository("rideRepository")
public class RideRepositoryImpl implements RideRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public Ride createRide(Ride ride) {
//		jdbcTemplate.update("insert into ride (name, duration) values (?, ?)", ride.getName(), ride.getDuration());
		
		SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
		
		List<String> columns = new ArrayList<>();
		columns.add("name");
		columns.add("duration");
		
		Map<String, Object> data = new HashMap<>();
		data.put("name", ride.getName());
		data.put("duration", ride.getDuration());
		insert.setTableName("ride");
		insert.setColumnNames(columns);
		insert.setGeneratedKeyName("id");
		
		Number id = insert.executeAndReturnKey(data);
		
		
		return getRide(id.intValue());
	}
	
	@Override
	public Ride getRide(Integer id) {
		Ride ride = jdbcTemplate.queryForObject("select * from ride where id = ?", 	new RideRowMapper(), id);
		return ride;
	}

	/**
	 * Using the FunctionalInterface RowMapper to map resultset to ride object
	 */
	@Override
	public List<Ride> getRides() {
		List <Ride> rides = jdbcTemplate.query("select * from ride", new RideRowMapper());

		return rides;
	}
	
	/**
	 * Unused SimpleJdbcInsert method
	 */
	public Ride createRideUsingSimpleJdbcInsert(Ride ride) {
		SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
		
		List<String> columns = new ArrayList<>();
		columns.add("name");
		columns.add("duration");
		
		Map<String, Object> data = new HashMap<>();
		data.put("name", ride.getName());
		data.put("duration", ride.getDuration());
		insert.setTableName("ride");
		insert.setColumnNames(columns);
		insert.setGeneratedKeyName("id");
		
		Number key = insert.executeAndReturnKey(data);
		System.out.println("Returned key: " + key);
		
		return null;
	}
	
	/**
	 * KeyHolder to get key back
	 */
	
	public Ride createRideGetKeyUsingKeyHolder(Ride ride) {
//		jdbcTemplate.update("insert into ride (name, duration) values (?, ?)", ride.getName(), ride.getDuration());
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

				PreparedStatement ps = con.prepareStatement("insert into ride (name, duration) values (?, ?)", new String[] {"id"});
				ps.setString(1, ride.getName());
				ps.setInt(2, ride.getDuration());
				return ps;
			}
		}, keyHolder);
		
		Number id = keyHolder.getKey();
		
		
		return getRide(id.intValue());
	}

	@Override
	public Ride updateRide(Ride ride) {
		jdbcTemplate.update("update ride set name = ?, duration = ? where id = ?", ride.getName(), ride.getDuration(), ride.getId());
		return ride;
	}
	
}
