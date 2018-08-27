/* This file tests to see if an Assignment submission contains the methods
   specified in the Javadoc specification. It checks for presence of classes and
   presence of methods with correct names, arguments, access and thrown
   exceptions.

   This file does *not* contain functionality tests or checks for spurious
   imports. Using these tests do not guarantee any marks, rather the
   file is just provided in case it is helpful.*/

import org.junit.*;
import org.junit.runners.model.InitializationError;
import org.junit.runner.notification.RunNotifier;
import static org.junit.Assert.*;
import org.junit.runners.JUnit4;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.rules.Timeout;

import java.lang.reflect.*;


public class TestMethods {


    // set a 20 second timeout on all test cases.
    @Rule
    public Timeout gt = new Timeout(20*1000);

    // constants for NO_ARGS and NO_EXCEPTIONS
    // are just empty arrays
    public static String [] NO_ARGS = {};
    public static String [] NO_EXCEPTIONS = {};

    private static class ExpectedMethod {
        /* A helper class to store information
           about a method. The information is checked
           using the reflection API (below).*/

        public String methodName;
        public String returnType;
        public List<String> argumentTypes;
        public List<String> exceptionTypes;

        public ExpectedMethod(String methodName, String returnType,
                              String [] argumentTypes) {
            /* Constructors in this class are just
               a quick way to fill in the fields. */
            this.methodName = methodName;
            this.returnType = returnType;
            this.argumentTypes =
                new ArrayList<>(Arrays.<String>asList(argumentTypes));
            this.exceptionTypes =
                new ArrayList<>(Arrays.<String>asList(NO_EXCEPTIONS));
        }

        public ExpectedMethod(String methodName, String returnType,
                              String [] argumentTypes,
                              String [] exceptionTypes) {
            /* Constructors in this class are just
               a quick way to fill in the fields. */
            this.methodName = methodName;
            this.returnType = returnType;
            this.argumentTypes =
                new ArrayList<>(Arrays.<String>asList(argumentTypes));
            this.exceptionTypes =
                new ArrayList<>(Arrays.<String>asList(exceptionTypes));
        }

    }

    @Test
    public void testBlockMethods() {

        // check that Block is an interface
        assertTrue((Block.class.getModifiers() & Modifier.INTERFACE) > 0);

        // check for expected members
        List<ExpectedMethod> expectedFields = new ArrayList<ExpectedMethod>();
        expectedFields.add(new ExpectedMethod("getColour", "java.lang.String",
                                              NO_ARGS, NO_EXCEPTIONS));
        expectedFields.add(new ExpectedMethod("getBlockType",
                                              "java.lang.String", NO_ARGS,
                                              NO_EXCEPTIONS));
        expectedFields.add(new ExpectedMethod("isDiggable", "boolean", NO_ARGS,
                                              NO_EXCEPTIONS));
        expectedFields.add(new ExpectedMethod("isMoveable", "boolean", NO_ARGS,
                                              NO_EXCEPTIONS));
        expectedFields.add(new ExpectedMethod("isCarryable", "boolean", NO_ARGS,
                                              NO_EXCEPTIONS));

        checkExpectedMethods(Block.class.getMethods(), expectedFields);

        assertTrue("The number of public members (" +
                   countPublicMembersIn(Block.class) +
                   ") is different from the specification (" +
                   expectedFields.size() + ")",
                   countPublicMembersIn(Block.class) == expectedFields.size());

        assertTrue("Class contains public fields.",
                   countPublicVariablesIn(Block.class) == 0);
    }

    @Test
    public void testBlockWorldException() {
        // check that BlockWorldException is not an interface and not abastract
        assertFalse((BlockWorldException.class.getModifiers() &
                     Modifier.INTERFACE) > 0);
        assertFalse((BlockWorldException.class.getModifiers() &
                     Modifier.ABSTRACT) > 0);

        // check that BlockWorldException extends Exception
        assertTrue(BlockWorldException.class.getSuperclass().getName().equals(
                       "java.lang.Exception"));

        assertTrue("Class contains public fields.",
                   countPublicVariablesIn(BlockWorldException.class) == 0);
    }

    @Test
    public void testBuilderMethods() {

        // check that Builder is not an interface and not abstract
        assertFalse((Builder.class.getModifiers() & Modifier.INTERFACE) > 0);
        assertFalse((Builder.class.getModifiers() & Modifier.ABSTRACT) > 0);

        // check for constructors
        try {
            Constructor<Builder> constructor =
                Builder.class.getDeclaredConstructor(String.class,
                                                     Tile.class);
            assertTrue("Builder constructor with args (Tile, List) " +
                       " throws wrong number of exceptions (" +
                       constructor.getExceptionTypes().length + ")",
                       constructor.getExceptionTypes().length == 0);
        } catch(NoSuchMethodException noSuchMethod) {
            fail("Missing builder constructor with args (Tile)");
        }

        try {
            Constructor<Builder> constructor =
                Builder.class.getDeclaredConstructor(String.class,
                                                     Tile.class,
                                                     List.class);
            assertTrue("Builder constructor with args (Tile, List) " +
                       " throws wrong number of exceptions (" +
                       constructor.getExceptionTypes().length + ")",
                       constructor.getExceptionTypes().length == 1);
            assertTrue(
                "Builder constructor with args (Tile, List) does not " +
                "throw InvalidBlockException",
                constructor.getExceptionTypes()[0].getName().equals(
                    "InvalidBlockException"));
        } catch(NoSuchMethodException noSuchMethod) {
            fail("Missing builder constructor with args (Tile, List)");
        }

        // check for expected members
        List<ExpectedMethod> expectedFields = new ArrayList<ExpectedMethod>();
        expectedFields.add(new ExpectedMethod("getName", "java.lang.String",
                                              NO_ARGS, NO_EXCEPTIONS));
        expectedFields.add(new ExpectedMethod("getCurrentTile", "Tile",
                                              NO_ARGS,
                                              NO_EXCEPTIONS));
        expectedFields.add(new ExpectedMethod("getInventory", "java.util.List",
                                              NO_ARGS, NO_EXCEPTIONS));
        String [] dropFromInventoryArgs = {"int"};
        String [] dropFromInventoryExceptions =
        {"InvalidBlockException", "TooHighException"};
        expectedFields.add(new ExpectedMethod("dropFromInventory", "void",
                                              dropFromInventoryArgs,
                                              dropFromInventoryExceptions));
        String [] digOnCurrentTileExceptions =
        {"InvalidBlockException", "TooLowException"};
        expectedFields.add(new ExpectedMethod("digOnCurrentTile", "void",
                                              NO_ARGS,
                                              digOnCurrentTileExceptions));
        String [] canEnterArgs = {"Tile"};
        expectedFields.add(new ExpectedMethod("canEnter", "boolean",
                                              canEnterArgs, NO_EXCEPTIONS));
        String [] moveToArgs = {"Tile"};
        String [] moveToExceptions = {"NoExitException"};
        expectedFields.add(new ExpectedMethod("moveTo", "void", moveToArgs,
                                              moveToExceptions));
        checkExpectedMethods(Builder.class.getMethods(), expectedFields);

        assertTrue("The number of public members (" +
                   (countPublicMembersIn(Builder.class) -
                    countPublicMembersIn(Object.class)) +
                   ") is different from the specification (" +
                   expectedFields.size() + ")",
                   countPublicMembersIn(
                       Builder.class) ==
                   expectedFields.size() + countPublicMembersIn(Object.class));

        assertTrue("Class contains public fields.",
                   countPublicVariablesIn(Builder.class) == 0);
    }

    @Test
    public void testGrassBlockMethods() {

        // check that GrassBlock is not an interface, and not abstract
        assertFalse((GrassBlock.class.getModifiers() &
                     Modifier.INTERFACE) > 0);
        assertFalse((GrassBlock.class.getModifiers() &
                     Modifier.ABSTRACT) > 0);

        // check that GrassBlock extends GroundBlock
        assertTrue(SoilBlock.class.getSuperclass().getName().equals(
                       "GroundBlock"));

        // check for expected members
        List<ExpectedMethod> expectedFields = new ArrayList<ExpectedMethod>();
        expectedFields.add(new ExpectedMethod("getColour", "java.lang.String",
                                              NO_ARGS));
        expectedFields.add(new ExpectedMethod("getBlockType",
                                              "java.lang.String", NO_ARGS));
        expectedFields.add(new ExpectedMethod("isDiggable", "boolean",
                                              NO_ARGS));
        expectedFields.add(new ExpectedMethod("isMoveable", "boolean",
                                              NO_ARGS));
        expectedFields.add(new ExpectedMethod("isCarryable", "boolean",
                                              NO_ARGS));
        checkExpectedMethods(GrassBlock.class.getMethods(), expectedFields);

        assertTrue("The number of public members (" +
                   (countPublicMembersIn(GrassBlock.class) -
                    countPublicMembersIn(Object.class)) +
                   ") is different from the specification (" +
                   expectedFields.size() + ")",
                   countPublicMembersIn(
                       GrassBlock.class) == expectedFields.size() +
                   countPublicMembersIn(Object.class));

        assertTrue("Class contains public fields.",
                   countPublicVariablesIn(GrassBlock.class) == 0);
    }

    @Test
    public void testGroundBlockMethods() {

        // check that Ground is not an interface, and is abstract
        assertFalse((GroundBlock.class.getModifiers() &
                     Modifier.INTERFACE) > 0);
        assertTrue((GroundBlock.class.getModifiers() &
                    Modifier.ABSTRACT) > 0);

        // check that GroundBlock implements Block
        List<Class<?>> interfaces =
            Arrays.asList(GroundBlock.class.getInterfaces());
        assertTrue(interfaces.contains(Block.class));

        // check for expected members
        List<ExpectedMethod> expectedFields = new ArrayList<ExpectedMethod>();
        expectedFields.add(new ExpectedMethod("getColour", "java.lang.String",
                                              NO_ARGS));
        expectedFields.add(new ExpectedMethod("getBlockType",
                                              "java.lang.String", NO_ARGS));
        expectedFields.add(new ExpectedMethod("isDiggable", "boolean",
                                              NO_ARGS));
        expectedFields.add(new ExpectedMethod("isMoveable", "boolean",
                                              NO_ARGS));
        expectedFields.add(new ExpectedMethod("isCarryable", "boolean",
                                              NO_ARGS));
        checkExpectedMethods(GroundBlock.class.getMethods(), expectedFields);

        assertTrue("The number of public members (" +
                   (countPublicMembersIn(GroundBlock.class) -
                    countPublicMembersIn(Object.class)) +
                   ") is different from the specification (" +
                   expectedFields.size() + ")",
                   countPublicMembersIn(
                       GroundBlock.class) == expectedFields.size() +
                   countPublicMembersIn(Object.class));

        assertTrue("Class contains public fields.",
                   countPublicVariablesIn(GroundBlock.class) == 0);
    }

    @Test
    public void testInvalidBlockException() {
        // check that BlockWorldException is not an interface and not abastract
        assertFalse((InvalidBlockException.class.getModifiers() &
                     Modifier.INTERFACE) > 0);
        assertFalse((InvalidBlockException.class.getModifiers() &
                     Modifier.ABSTRACT) > 0);

        // check that BlockWorldException extends Exception
        assertTrue(InvalidBlockException.class.getSuperclass()
                   .getName().equals("BlockWorldException"));

        assertTrue("Class contains public fields.",
                   countPublicVariablesIn(InvalidBlockException.class) == 0);

        // check for default constructor
        try {
            Constructor<InvalidBlockException> constructor =
                InvalidBlockException.class.getDeclaredConstructor();
        } catch(NoSuchMethodException noSuchMethod) {
            fail("Missing builder constructor with args (Tile)");
        }

        assertTrue("Class contains additional constructors.",
                    InvalidBlockException.class.getConstructors().length == 1);
    }

    @Test
    public void testNoExitException() {
        // check that BlockWorldException is not an interface and not abastract
        assertFalse((NoExitException.class.getModifiers() &
                     Modifier.INTERFACE) > 0);
        assertFalse((NoExitException.class.getModifiers() &
                     Modifier.ABSTRACT) > 0);

        // check that BlockWorldException extends Exception
        assertTrue(NoExitException.class.getSuperclass()
                   .getName().equals("BlockWorldException"));


        assertTrue("Class contains public fields.",
                   countPublicVariablesIn(NoExitException.class) == 0);

      // check for default constructor
        try {
            Constructor<NoExitException> constructor =
                NoExitException.class.getDeclaredConstructor();
        } catch(NoSuchMethodException noSuchMethod) {
            fail("Missing builder constructor with args (Tile)");
        }

        assertTrue("Class contains additional constructors.",
                    NoExitException.class.getConstructors().length == 1);
    }

    @Test
    public void testSoilBlockMethods() {

        // check that Ground is not an interface, and is abstract
        assertFalse((SoilBlock.class.getModifiers() &
                     Modifier.INTERFACE) > 0);
        assertFalse((SoilBlock.class.getModifiers() &
                     Modifier.ABSTRACT) > 0);

        // check that SoilBlock extends GroundBlock
        assertTrue(SoilBlock.class.getSuperclass()
                   .getName().equals("GroundBlock"));

        // check for expected members
        List<ExpectedMethod> expectedFields = new ArrayList<ExpectedMethod>();
        expectedFields.add(new ExpectedMethod("getColour", "java.lang.String",
                                              NO_ARGS));
        expectedFields.add(new ExpectedMethod("getBlockType",
                                              "java.lang.String", NO_ARGS));
        expectedFields.add(new ExpectedMethod("isDiggable", "boolean",
                                              NO_ARGS));
        expectedFields.add(new ExpectedMethod("isMoveable", "boolean",
                                              NO_ARGS));
        expectedFields.add(new ExpectedMethod("isCarryable", "boolean",
                                              NO_ARGS));
        checkExpectedMethods(SoilBlock.class.getMethods(), expectedFields);

        assertTrue("The number of public members (" +
                   (countPublicMembersIn(SoilBlock.class) -
                    countPublicMembersIn(Object.class)) +
                   ") is different from the specification (" +
                   expectedFields.size() + ")",
                   countPublicMembersIn(
                       SoilBlock.class) == expectedFields.size() +
                   countPublicMembersIn(Object.class));

        assertTrue("Class contains public fields.",
                   countPublicVariablesIn(SoilBlock.class) == 0);
    }

    @Test
    public void testStoneBlockMethods() {

        // check that Ground is not an interface, and is abstract
        assertFalse((StoneBlock.class.getModifiers() &
                     Modifier.INTERFACE) > 0);
        assertFalse((StoneBlock.class.getModifiers() &
                     Modifier.ABSTRACT) > 0);

        // check that StoneBlock implements Block
        List<Class<?>> interfaces =
            Arrays.asList(StoneBlock.class.getInterfaces());
        assertTrue(interfaces.contains(Block.class));

        // check for expected members
        List<ExpectedMethod> expectedFields = new ArrayList<ExpectedMethod>();
        expectedFields.add(new ExpectedMethod("getColour", "java.lang.String",
                                              NO_ARGS));
        expectedFields.add(new ExpectedMethod("getBlockType",
                                              "java.lang.String", NO_ARGS));
        expectedFields.add(new ExpectedMethod("isDiggable", "boolean",
                                              NO_ARGS));
        expectedFields.add(new ExpectedMethod("isMoveable", "boolean",
                                              NO_ARGS));
        expectedFields.add(new ExpectedMethod("isCarryable", "boolean",
                                              NO_ARGS));
        checkExpectedMethods(StoneBlock.class.getMethods(), expectedFields);

        assertTrue("The number of public members (" +
                   (countPublicMembersIn(StoneBlock.class) -
                    countPublicMembersIn(Object.class)) +
                   ") is different from the specification (" +
                   expectedFields.size() + ")",
                   countPublicMembersIn(
                       StoneBlock.class) == expectedFields.size() +
                   countPublicMembersIn(Object.class));

        assertTrue("Class contains public fields.",
                   countPublicVariablesIn(StoneBlock.class) == 0);
    }

    @Test
    public void testTileMethods() throws NoSuchMethodException {
        // check that Tile is not an interface and not abastract
        assertFalse((Tile.class.getModifiers() & Modifier.INTERFACE) > 0);
        assertFalse((Tile.class.getModifiers() & Modifier.ABSTRACT) > 0);

        // check for constructors -- these will throw an exception if the
        // constructor is not there.
        try {
            Constructor<Tile> constructor =
                Tile.class.getDeclaredConstructor();
            assertTrue("Tile constructor with args () " +
                       "throws wrong number of exceptions (" +
                       constructor.getExceptionTypes().length + ")",
                       constructor.getExceptionTypes().length == 0);
        } catch(NoSuchMethodException noSuchMethod) {
            fail("Missing Tile constructor with args ()");
        }

        try {
            Constructor<Tile> constructor =
                Tile.class.getDeclaredConstructor(List.class);
            assertTrue("Tile constructor with args (List) "+
                       "throws wrong number of exceptions (" +
                       constructor.getExceptionTypes().length + ")",
                       constructor.getExceptionTypes().length == 1);
            assertTrue(
                "Tile constructor with args (Tile, List) "+
                " does not throw TooHighException",
                constructor.getExceptionTypes()[0].getName().equals(
                    "TooHighException"));
        } catch(NoSuchMethodException noSuchMethod) {
            fail("Missing Tile constructor with args (List)");
        }

        // check for expected members
        List<ExpectedMethod> expectedFields = new ArrayList<ExpectedMethod>();
        expectedFields.add(new ExpectedMethod("getExits", "java.util.Map",
                                              NO_ARGS));
        expectedFields.add(new ExpectedMethod("getBlocks", "java.util.List",
                                              NO_ARGS));
        String [] getTopBlockExceptions = {"TooLowException"};
        expectedFields.add(new ExpectedMethod("getTopBlock", "Block", NO_ARGS,
                                              getTopBlockExceptions));
        String [] removeTopBlockExceptions = {"TooLowException"};
        expectedFields.add(new ExpectedMethod("removeTopBlock", "void",
                                              NO_ARGS,
                                              removeTopBlockExceptions));
        String [] addExitArgs = {"java.lang.String", "Tile"};
        String [] addExitExceptions = {"NoExitException"};
        expectedFields.add(new ExpectedMethod("addExit", "void", addExitArgs,
                                              addExitExceptions));
        String [] removeExitArgs = {"java.lang.String"};
        String [] removeExitExceptions = {"NoExitException"};
        expectedFields.add(new ExpectedMethod("removeExit", "void",
                                              removeExitArgs,
                                              removeExitExceptions));
        String [] digExceptions = {"TooLowException",
                                   "InvalidBlockException"};
        expectedFields.add(new ExpectedMethod("dig", "Block", NO_ARGS,
                                              digExceptions));
        String [] moveBlockArgs = {"java.lang.String"};
        String [] moveBlockExceptions =
        {"TooHighException", "InvalidBlockException", "NoExitException"};
        expectedFields.add(new ExpectedMethod("moveBlock", "void",
                                              moveBlockArgs,
                                              moveBlockExceptions));
        String [] placeBlockArgs = {"Block"};
        String [] placeBlockExceptions =
        {"TooHighException", "InvalidBlockException"};
        expectedFields.add(new ExpectedMethod("placeBlock", "void",
                                              placeBlockArgs,
                                              placeBlockExceptions));
        checkExpectedMethods(Tile.class.getMethods(), expectedFields);

        assertTrue("The number of public members (" +
                   (countPublicMembersIn(Tile.class) -
                    countPublicMembersIn(Object.class)) +
                   ") is different from the specification (" +
                   expectedFields.size() + ")",
                   countPublicMembersIn(Tile.class) == expectedFields.size() +
                   countPublicMembersIn(Object.class));

        assertTrue("Class contains public fields.",
                   countPublicVariablesIn(Tile.class) == 0);
    }

    @Test
    public void testTooHighException() {
        // check that TooHighException is not an interface and not abastract
        assertFalse((TooHighException.class.getModifiers() &
                     Modifier.INTERFACE) > 0);
        assertFalse((TooHighException.class.getModifiers() &
                     Modifier.ABSTRACT) > 0);

        // check that BlockWorldException extends Exception
        assertTrue(TooHighException.class.getSuperclass()
                   .getName().equals("BlockWorldException"));

        assertTrue("Class contains public fields.",
                   countPublicVariablesIn(TooHighException.class) == 0);

      	// check for default constructor
        try {
            Constructor<TooHighException> constructor =
                TooHighException.class.getDeclaredConstructor();
        } catch(NoSuchMethodException noSuchMethod) {
            fail("Missing builder constructor with args (Tile)");
        }

        assertTrue("Class contains additional constructors.",
                    TooHighException.class.getConstructors().length == 1);
    }

    @Test
    public void testTooLowException() {
        // check that TooLowException is not an interface and not abastract
        assertFalse(
            (TooLowException.class.getModifiers() & Modifier.INTERFACE) > 0);
        assertFalse(
            (TooLowException.class.getModifiers() & Modifier.ABSTRACT) > 0);

        // check that BlockWorldException extends Exception
        assertTrue(TooLowException.class.getSuperclass()
                   .getName().equals("BlockWorldException"));

        assertTrue("Class contains public fields.",
                   countPublicVariablesIn(TooLowException.class) == 0);

      	// check for default constructor
        try {
            Constructor<TooLowException> constructor =
                TooLowException.class.getDeclaredConstructor();
        } catch(NoSuchMethodException noSuchMethod) {
            fail("Missing builder constructor with args (Tile)");
        }

        assertTrue("Class contains additional constructors.",
                    TooLowException.class.getConstructors().length == 1);
    }

    @Test
    public void testWoodBlockMethods() {

        // check that Ground is not an interface, and is abstract
        assertFalse((WoodBlock.class.getModifiers() &
                     Modifier.INTERFACE) > 0);
        assertFalse((WoodBlock.class.getModifiers() &
                     Modifier.ABSTRACT) > 0);

        // check that StoneBlock implements Block
        List<Class<?>> interfaces =
            Arrays.asList(WoodBlock.class.getInterfaces());
        assertTrue(interfaces.contains(Block.class));

        // check for expected members
        List<ExpectedMethod> expectedFields = new ArrayList<ExpectedMethod>();
        expectedFields.add(new ExpectedMethod("getColour", "java.lang.String",
                                              NO_ARGS));
        expectedFields.add(new ExpectedMethod("getBlockType",
                                              "java.lang.String", NO_ARGS));
        expectedFields.add(new ExpectedMethod("isDiggable", "boolean",
                                              NO_ARGS));
        expectedFields.add(new ExpectedMethod("isMoveable", "boolean",
                                              NO_ARGS));
        expectedFields.add(new ExpectedMethod("isCarryable", "boolean",
                                              NO_ARGS));
        checkExpectedMethods(WoodBlock.class.getMethods(), expectedFields);

        assertTrue("The number of public members (" +
                   (countPublicMembersIn(WoodBlock.class) -
                    countPublicMembersIn(Object.class)) +
                   ") is different from the specification (" +
                   expectedFields.size() + ")",
                   countPublicMembersIn(
                       WoodBlock.class) == expectedFields.size() +
                   countPublicMembersIn(Object.class));

        assertTrue("Class contains public fields.",
                   countPublicVariablesIn(WoodBlock.class) == 0);
    }

    /*
     * Count the number of public member functions in a class.
     */
    private int countPublicMembersIn(Class klass) {
        int publicMembers = 0;

        for (Method method : klass.getMethods()) {
            if ((method.getModifiers() & Modifier.PUBLIC) > 0) {
                publicMembers++;
            }
        }

        return publicMembers;
    }

    /*
     * Count the number of public variables in a class.
     */
    private int countPublicVariablesIn(Class klass) {
        int publicVariables = 0;

        for (Field field : klass.getFields()) {
            if ((field.getModifiers() & Modifier.PUBLIC) > 0) {
                publicVariables++;
            }
        }

        return publicVariables;
    }

    /*
     * Check that expected fields all exist within methods using assertions.
     */
    private void checkExpectedMethods(Method [] methods,
                                      List<ExpectedMethod> expectedFields) {

        List<String> methodNames = new ArrayList<>();
        for (Method method : methods) {
            methodNames.add(method.getName());
        }

        for (ExpectedMethod expectedMethod : expectedFields) {

            // check if field exists
            Method method = null;

            for (int i = 0; i < methods.length; i++) {
                // if name and arguments match
                if (methods[i].getName() == expectedMethod.methodName &&
                    checkArgumentTypes(methods[i],
                                       expectedMethod.argumentTypes)) {
                    method = methods[i];
                }
            }

            // if no matching method is found, method will be null
            assertTrue(
                "No method matching " + expectedMethod.methodName +
                " with args " + expectedMethod.argumentTypes,
                method != null);

            // check that the type is the expected type
            assertTrue(
                "Mismatched return type (" + expectedMethod.returnType +
                ") for  " + expectedMethod.methodName + " with args " +
                expectedMethod.argumentTypes + " that returns (" +
                method.getReturnType().getName() + ")",
                method.getReturnType().getName().equals(expectedMethod.
                                                        returnType));

            // check that the field is public
            assertTrue(
                "Method " + expectedMethod.methodName + " with args " +
                expectedMethod.argumentTypes + " is not public",
                (method.getModifiers() & Modifier.PUBLIC) > 0);

            // check that the correct exceptions are thrown
            assertTrue("Method " + expectedMethod.methodName +
                       " with args " + expectedMethod.argumentTypes +
                       " does not throw the correct exceptions",
                       checkThrownExceptions(
                           method,
                           expectedMethod.exceptionTypes));
        }
    }

    /*
     * Check that the parameters match those expected.
     */
    private boolean checkArgumentTypes(Method method,
                                       List<String> argumentTypes) {

        if (method.getParameterTypes().length != argumentTypes.size()) {
            return false;
        }

        for (int i = 0; i < argumentTypes.size(); i++) {
            if (!method.getParameterTypes()[i].getName().equals(
                    argumentTypes.get(i))) {
                return false;
            }
        }

        return true;
    }

    /*
     * Check that the thrown exceptions match those expected.
     */
    private boolean checkThrownExceptions(Method method,
                                          List<String> exceptionTypes) {
        if (method.getExceptionTypes().length != exceptionTypes.size()) {
            System.out.println("Sizes mismatch");
            return false;
        }

        List<Class> methodExceptionTypes =
            new ArrayList<>(Arrays.asList(method.getExceptionTypes()));

        // sort both
        methodExceptionTypes.sort(new java.util.Comparator<Class>() {
            @Override public int compare(Class klass1, Class klass2) {
                return klass1.getName().compareTo(klass2.getName());
            }
        });
        exceptionTypes.sort(null);

        for (int i = 0; i < exceptionTypes.size(); i++) {
            if (!methodExceptionTypes.get(i).getName().equals(exceptionTypes
                                                              .get(i))) {
                System.out.println(methodExceptionTypes.get(i));
                System.out.println(exceptionTypes.get(i));
                return false;
            }
        }

        return true;
    }

}
