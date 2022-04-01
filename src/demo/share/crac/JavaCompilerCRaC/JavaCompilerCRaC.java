import java.util.Arrays;
import jdk.crac.Core;
import jdk.crac.Context;
import jdk.crac.Resource;
import java.util.Optional;

public class JavaCompilerCRaC implements Resource {

    static void runJavac(String... args) {
        System.out.println("javac " + String.join(" ", args));
        int status = com.sun.tools.javac.Main.compile(args);
        if (status != 0) {
            System.exit(status);
        }
    }

    static String[] appendJava(String... args) {
	return Arrays.stream(args).map(str -> str.concat(".java")).toArray(String[]::new);
    }

    public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
	/* nothing to do */
    }

    public void afterRestore(Context<? extends Resource> context) throws Exception {
        Optional<String[]> optionalArgs = context.getNewArgs();
	if (optionalArgs.isPresent()) {
	    runJavac(appendJava(optionalArgs.get()));
	}
    }

    public static void main(String... args) throws Exception {
        int startIdx = 0;
        for (int endIdx = 1; endIdx < args.length; ++endIdx) {
            if (args[endIdx].equals("--")) {
                runJavac(appendJava(Arrays.copyOfRange(args, startIdx, endIdx)));
                startIdx = endIdx + 1;
            }
        }

        if (startIdx < args.length) {
            runJavac(appendJava(Arrays.copyOfRange(args, startIdx, args.length)));
        }
	Core.getGlobalContext().register(new JavaCompilerCRaC());
        Core.checkpointRestore();
    }
}
