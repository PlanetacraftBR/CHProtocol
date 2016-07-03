package protocolsupportbuildprocessor.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;
import protocolsupportbuildprocessor.annotations.NeedsNoArgConstructor;

@SupportedAnnotationTypes({"*"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class HasNoArgConstructorProcessor
  extends AbstractProcessor
{
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
  {
    for (Element type : roundEnv.getRootElements()) {
      if ((type.getKind() == ElementKind.CLASS) && (!type.getModifiers().contains(Modifier.ABSTRACT)) && 
        (hasNeedsNoArgsConstrAnnotationInHierarcy((TypeElement)type))) {
        checkForNoArgConstructor(type);
      }
    }
    return true;
  }
  
  private boolean hasNeedsNoArgsConstrAnnotationInHierarcy(TypeElement type)
  {
    ArrayList<TypeMirror> hierarcy = new ArrayList();
    for (;;)
    {
      hierarcy.add(type.asType());
      hierarcy.addAll(type.getInterfaces());
      TypeMirror typemirror = type.getSuperclass();
      if ((typemirror.getKind() == TypeKind.NONE) || (!(typemirror instanceof DeclaredType))) {
        break;
      }
      type = (TypeElement)((DeclaredType)typemirror).asElement();
    }
    for (TypeMirror hierarcyElement : hierarcy) {
      if ((hierarcyElement instanceof DeclaredType))
      {
        DeclaredType decltype = (DeclaredType)hierarcyElement;
        if (decltype.asElement().getAnnotation(NeedsNoArgConstructor.class) != null) {
          return true;
        }
      }
    }
    return false;
  }
  
  private void checkForNoArgConstructor(Element type)
  {
    for (Element enclosed : type.getEnclosedElements()) {
      if ((enclosed.getKind() == ElementKind.CONSTRUCTOR) && 
        (((ExecutableElement)enclosed).getParameters().isEmpty())) {
        return;
      }
    }
    this.processingEnv.getMessager().printMessage(Kind.ERROR, "Missing a no-arg constructor", type);
  }
}
