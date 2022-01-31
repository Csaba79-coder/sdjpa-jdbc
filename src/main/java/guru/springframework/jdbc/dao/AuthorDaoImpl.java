package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class AuthorDaoImpl implements AuthorDao {


    private final DataSource source;

    public AuthorDaoImpl(DataSource source) {
                this.source = source;
    }

    @Override
    public Author getById(Long id) {
        Connection connection = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            // statement = connection.createStatement();
            // resultSet = statement.executeQuery("SELECT * FROM author where id = " + id);

            preparedStatement = connection.prepareStatement("SELECT * FROM author where id = ?");
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Author author = new Author();
                author.setId(id);
                author.setFirstName(resultSet.getString("first_name")); // here can come column name or index!
                author.setLastName(resultSet.getString("last_name"));

                return author;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                /*if (statement != null) {
                    statement.close();
                }*/
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                   connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public DataSource getSource() {
        return source;
    }
}
