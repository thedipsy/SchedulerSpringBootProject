package mk.ukim.finki.wp.schedulerspringbootproject.Web.Controller;

import mk.ukim.finki.wp.schedulerspringbootproject.Config.Constants;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.OfficeDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.OfficeNotFoundException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.UniqueOrdinalNumberException;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.OfficeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/offices")
public class OfficeController {

    private final OfficeService officeService;

    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    /**
     * GET Method that returns a view with information about offices.
     * Also, provides a form for creating a new office.
     */
    @GetMapping
    public String getOfficesPage(@RequestParam(required = false) String error,
                                 @RequestParam(required = false) String errorMessage,
                                 Model model) {
        if (error != null) {
            model.addAttribute(Constants.HAS_ERROR, true);
            model.addAttribute(Constants.ERROR, errorMessage);
        }

        model.addAttribute(Constants.OFFICES, officeService.findAll());
        model.addAttribute(Constants.BODY_CONTENT, "offices");
        return "master-template";
    }

    /**
     * POST Method for creating a new office.
     */
    @PostMapping("/add-office")
    public String createOffice(@RequestParam int ordinal_number) {

        try {
            OfficeDto officeDto = new OfficeDto(ordinal_number);
            officeService.save(officeDto);

            return "redirect:/offices";
        } catch (UniqueOrdinalNumberException e) {
            return "redirect:/offices?error=true&errorMessage=" + e.getMessage();

        }
    }

    /**
     * DELETE Method that deletes the selected office.
     */
    @GetMapping("/delete-office/{office_id}")
    public String deleteOffice(@PathVariable int office_id){
        try{
            officeService.deleteById(office_id);
            return "redirect:/offices";
        } catch(OfficeNotFoundException e){
            return "redirect:/offices?error=true&errorMessage=" + e.getMessage();
        }
    }

}