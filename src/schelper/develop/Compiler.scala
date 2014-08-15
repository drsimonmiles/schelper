package schelper.develop

import java.io.{PrintWriter, File}
import schelper.core.{SavePosition, HideApplication, BasicForm}
import schelper.navigate.{HomeButton, BackButton, NavigationButton, Navigable}
import settings4scala.Settings
import schelper.core.SchelperConstants._
import Settings._
import scala.concurrent.Future
import scala.swing._
import scala.sys.process._
import scala.concurrent.ExecutionContext.Implicits.global

object Compiler extends BasicForm with Navigable {
  val linkName = "Compile"
  val linkColour = SectionColour
  val scalacField = field ("Scalac path", getFile (ApplicationName, ScalacPath).map (_.getAbsolutePath).getOrElse (""))
  val buildField = field ("Build path", getFile (ApplicationName, BuildPath).map (_.getAbsolutePath).getOrElse (""))
  val javaField = field ("Java path", getFile (ApplicationName, JavaPath).map (_.getAbsolutePath).getOrElse (""))
  val appClassField = field ("App class", get (ApplicationName, AppClass).getOrElse (""))
  val verboseCheck = checkbox ("Verbose", value = false)
  val saveButton = button (new BackButton ("Save paths and close", () => save (), NavigateColour))
  val trialButton = button (new HomeButton ("Trial compile", () => compile (), ActionColour))
  val rebuildButton = button (new HomeButton ("Rebuild app", () => rebuild (), ActionColour))
  val componentPublishers = List (saveButton, trialButton, rebuildButton)

  private def save () {
    def saveFile (field: TextField, setting: String) = if (!field.text.isEmpty) putFile (ApplicationName, setting, new File (field.text))
    def saveText (field: TextField, setting: String) = if (!field.text.isEmpty) put (ApplicationName, setting, field.text)
    saveFile (scalacField, ScalacPath)
    saveFile (buildField, BuildPath)
    saveFile (javaField, JavaPath)
    saveText (appClassField, AppClass)
  }

  private def compile () {
    val buildDir = new File (buildField.text)
    buildDir.mkdirs ()
    val sourceList = new File (buildDir, "sourceList")
    val listOut = new PrintWriter (sourceList)
    try {
      Sources.allScalaPaths.foreach (listOut.println)
    } finally {
      listOut.close ()
    }
    val classpath = Libraries.files.values.mkString (File.pathSeparator)
    val command = if (verboseCheck.selected)
      List (scalacField.text, "-verbose", "-classpath", classpath, "-d", buildField.text, "@" + sourceList.getAbsolutePath)
    else
      List (scalacField.text, "-classpath", classpath, "-d", buildField.text, "@" + sourceList.getAbsolutePath)
    val results = new OutputDialog (size)
    val io = ProcessLogger (line => results.log.text += (line + "\n"))
    results.pack ()
    results.visible = true
    command ! io
    results.log.text += "Completed"
    save ()
  }

  private def rebuild () {
    compile ()
    publish (SavePosition)
    val classpath = Libraries.files.values.mkString (File.pathSeparator) + File.pathSeparator + buildField.text
    val command = List (javaField.text, "-classpath", classpath, appClassField.text)
    val results = new OutputDialog (size)
    val io = ProcessLogger (line => results.log.text += (line + "\n"))
    results.pack ()
    results.visible = true
    Future {
      command ! io
    }
    results.log.text += "Started"
    publish (HideApplication)
  }
}

/*
scalac <options> <source files>
  -Dproperty=value           Pass -Dproperty=value directly to the runtime system.
  -J<flag>                   Pass <flag> directly to the runtime system.
  -P:<plugin>:<opt>          Pass an option to a plugin
  -X                         Print a synopsis of advanced options.
  -bootclasspath <path>      Override location of bootstrap class files.
  -classpath <path>          Specify where to find user class files.
  -d <directory|jar>         destination for generated classfiles.
  -dependencyfile <file>     Set dependency tracking file.
  -deprecation               Emit warning and location for usages of deprecated APIs.
  -encoding <encoding>       Specify character encoding used by source files.
  -explaintypes              Explain type errors in more detail.
  -extdirs <path>            Override location of installed extensions.
  -feature                   Emit warning and location for usages of features that should be imported explicitly.
  -g:<level>                 Set level of generated debugging info. (none,source,line,vars,notailcalls) default:vars
  -help                      Print a synopsis of standard options
  -javabootclasspath <path>  Override java boot classpath.
  -javaextdirs <path>        Override java extdirs classpath.
  -language:<feature>        Enable one or more language features: dynamics,postfixOps,reflectiveCalls,implicitConversions,higherKinds,existentials,experimental.macros.
  -no-specialization         Ignore @specialize annotations.
  -nobootcp                  Do not use the boot classpath for the scala jars.
  -nowarn                    Generate no warnings.
  -optimise                  Generates faster bytecode by applying optimisations to the program
  -print                     Print program with Scala-specific features removed.
  -sourcepath <path>         Specify location(s) of source files.
  -target:<target>           Target platform for object files. All JVM 1.5 targets are deprecated. (jvm-1.5,jvm-1.6,jvm-1.7) default:jvm-1.6
  -toolcp <path>             Add to the runner classpath.
  -unchecked                 Enable additional warnings where generated code depends on assumptions.
  -uniqid                    Uniquely tag all identifiers in debugging output.
  -usejavacp                 Utilize the java.class.path in classpath resolution.
  -usemanifestcp             Utilize the manifest in classpath resolution.
  -verbose                   Output messages about what the compiler is doing.
  -version                   Print product version and exit.
  @<file>                    A text file containing compiler arguments (options and source files)
 */