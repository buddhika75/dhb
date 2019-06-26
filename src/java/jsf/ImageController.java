/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;



import entity.Participant;
import java.io.ByteArrayInputStream;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author nilukagun
 */
@Named
@RequestScoped
public class ImageController {

   
    @Inject
    InstitutionController messageController;

    public InstitutionController getInstitutionController() {
        return messageController;
    }

   

    

    /**
     * Creates a new instance of ImageController
     */
    public ImageController() {
    }

    public StreamedContent getImage() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            System.out.println("renderer response");
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            System.out.println("NOT a renderer response");
            Participant ei = getInstitutionController().getSelectedParcipant();
            System.out.println("ei = " + ei);
            if (ei == null) {
                System.out.println("3");
                return new DefaultStreamedContent();
            } else {
                System.out.println("4");
                if (ei.getId() != null && ei.getBaImage() != null) {
                    System.out.println("5");
                    return new DefaultStreamedContent(new ByteArrayInputStream(ei.getBaImage()), ei.getFileType(), ei.getFileName());
                } else {
                    System.out.println("6");
                    return new DefaultStreamedContent();
                }
            }
        }

    }

   

}
