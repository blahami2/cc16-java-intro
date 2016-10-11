package cz.codecamp.logger.loggers;

import cz.codecamp.logger.FormatterInterface;
import cz.codecamp.logger.LogLevelEnum;
import cz.codecamp.logger.formatters.JsonFormatter;
import cz.codecamp.logger.formatters.SimpleFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by blaha on 06.10.2016.
 */
public class StdoutLoggerTest {
    private static final String TEXT_MESSAGE = "Test message";
    private static final FormatterInterface[] formatters = new FormatterInterface[]{ new SimpleFormatter(), new JsonFormatter() };

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.stream( LogLevelEnum.values() )
                .flatMap( logLevelEnum -> Arrays.stream( formatters )
                        .map( formatter -> new Object[]{ logLevelEnum, TEXT_MESSAGE, formatter } ) )
                .collect( Collectors.toList() );
    }

    private static final String FORMAT_DATE = "yyyy-MM-dd";

    final LogLevelEnum level;
    final String message;
    final FormatterInterface formatter;
    String formatted;
    ByteArrayOutputStream outContent;
    StdoutLogger logger;

    public StdoutLoggerTest( LogLevelEnum level, String message, FormatterInterface formatter ) {
        this.level = level;
        this.message = message;
        this.formatter = formatter;
    }

    @Before
    public void setUp() throws Exception {
        outContent = new ByteArrayOutputStream();
        System.setOut( new PrintStream( outContent ) );
        formatted = formatter.format( level, message );
        logger = new StdoutLogger();
    }

    @Test
    public void logFormatted() throws Exception {
        logger.logFormatted( level, message, formatted );
        assertEquals( formatted + System.lineSeparator(), outContent.toString() );
    }

    @Test
    public void close() throws Exception {
        // I don't expect this method to do anything
        logger.close();
    }

    @After
    public void tearDown() throws Exception {
        System.setOut( null );
    }
}