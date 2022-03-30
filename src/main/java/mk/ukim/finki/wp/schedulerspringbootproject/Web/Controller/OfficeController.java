package mk.ukim.finki.wp.schedulerspringbootproject.Web.Controller;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.DeskDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.OfficeDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Desk;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.DeskService;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.EmployeeService;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.OfficeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@Controller
@RequestMapping("/offices")
public class OfficeController {

    private final OfficeService officeService;
    private final DeskService deskService;
    private final EmployeeService employeeService;

    public OfficeController(OfficeService officeService, DeskService deskService, EmployeeService employeeService) {
        this.officeService = officeService;
        this.deskService = deskService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getOfficesPage(@RequestParam(required = false) String error,
                                 Model model) {
        if (error != null) {
            model.addAttribute("hasError", true);
        }

        if(officeService.findAll() == null) {
            model.addAttribute("offices", new ArrayList<String>());
        }
        model.addAttribute("offices", officeService.findAll());

        if(deskService.findAll() == null) {
            model.addAttribute("desks", new ArrayList<String>());
        }

        model.addAttribute("desks", deskService.findAll());
        model.addAttribute("employees", employeeService.findAll());

        model.addAttribute("bodyContent", "offices");

        return "master-template";
    }

    @PostMapping("/addOffice")
    public String createOffice(@RequestParam int ordinal_number) {

        try {
            OfficeDto officeDto = new OfficeDto(ordinal_number);
            officeService.save(officeDto);

            return "redirect:/offices";
        } catch (Exception e) {
            return "redirect:/offices?error=true";
        }
    }

    @PostMapping("/addDesk")
    public String createDesk(@RequestParam int ordinal_number,
                             @RequestParam int office_id,
                             @RequestParam (required = false) String employee_id) {

        try {
            DeskDto deskDto = new DeskDto(ordinal_number, office_id);

            Desk desk = deskService.save(deskDto);
            if(employee_id != null && !employee_id.isEmpty()){
                employeeService.assignDesk(employee_id, desk.getDeskId());
            }

            return "redirect:/offices";
        } catch (Exception e) {
            return "redirect:/offices?error=true";
        }
    }

    @GetMapping("/delete-office/{office_id}")
    public String deleteOffice(@PathVariable int office_id){
        try{
            officeService.deleteById(office_id);
            return "redirect:/offices";
        } catch(Exception e){
            return "redirect:/offices?error=true";
        }
    }

    @GetMapping("/delete-desk/{desk_id}")
    public String deleteDesk(@PathVariable int desk_id){
        try{
            deskService.deleteById(desk_id);
            return "redirect:/offices";
        } catch(Exception e){
            return "redirect:/offices?error=true";
        }
    }
}