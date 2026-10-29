package com.nexusmaven;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testClassesUseJava25Bytecode() throws IOException
    {
        try (InputStream classResource = AppTest.class.getResourceAsStream("AppTest.class"))
        {
            assertNotNull("Compiled test class must be available", classResource);
            try (DataInputStream classFile = new DataInputStream(classResource))
            {
                assertEquals(0xCAFEBABE, classFile.readInt());
                classFile.readUnsignedShort(); // Minor version
                assertEquals("Java 25 class-file version", 69, classFile.readUnsignedShort());
            }
        }
    }
}
