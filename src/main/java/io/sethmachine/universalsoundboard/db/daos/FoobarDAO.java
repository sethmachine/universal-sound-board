package io.sethmachine.universalsoundboard.db.daos;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface FoobarDAO {
  @SqlUpdate("create table something (id int primary key, name varchar(100))")
  void createSomethingTable();

  @SqlUpdate("create table clicks (id int primary key, name varchar(100))")
  void createClickTable();

  @SqlUpdate("insert into something (id, name) values (:id, :name)")
  void insert(@Bind("id") int id, @Bind("name") String name);

  @SqlUpdate("insert into clicks (id, name) values (:id, :name)")
  void insertClick(@Bind("id") int id, @Bind("name") String name);

  @SqlQuery("select name from something where id = :id")
  String findNameById(@Bind("id") int id);

  @SqlQuery("select name from clicks where id = :id")
  String findClickById(@Bind("id") int id);
}
