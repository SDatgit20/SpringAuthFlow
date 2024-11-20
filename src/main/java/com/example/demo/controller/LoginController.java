package com.example.demo.controller;


import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.security.authentication.AuthenticationManager;


import com.example.demo.dto.signupdto;
import com.example.demo.file.fileStorageService;
import com.example.demo.file.fileUploadResponse;
import com.example.demo.jwt.jwtResponse;
import com.example.demo.jwt.jwtUtil;
import com.example.demo.model.loginm;
import com.example.demo.model.status;
import com.example.demo.repo.loginrepo;
import com.example.demo.repo.statusRepo;


@RestController
@CrossOrigin("http://localhost:8081")
public class LoginController {
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
	loginrepo repo;
	
	@Autowired
	PasswordEncoder pe;
	
	@Autowired
	private com.example.demo.security.cuds cuds;
	
	@Autowired
	private fileStorageService fss;
	
	@Autowired
	private jwtUtil jwtu;
	
	@Autowired
	private statusRepo srepo;
	
	@PostMapping("/login")
	public ResponseEntity<?> generateToken(@RequestBody loginm jwtr) throws Exception{
		try {
			//System.out.println(jwtr.getEmail());
			//System.out.print("hiii");
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtr.getEmail(),jwtr.getPass()));
			//System.out.print("hiii");
		}
		catch(UsernameNotFoundException e){
			throw new Exception("Bad creds");
		}
		UserDetails ud=this.cuds.loadUserByUsername(jwtr.getEmail());
		System.out.println(ud);
		String token=this.jwtu.generateToken(ud);
		//System.out.println("hello");
		
		return ResponseEntity.ok(new jwtResponse(token));
	}
    
	@PostMapping("/uploadFile")
	public fileUploadResponse uploadFile(@RequestParam("file") MultipartFile file) {
		String fileName=fss.storeFile(file);
		String uri=ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/downloadFile")
				.path(fileName)
				.toUriString();
		return new fileUploadResponse(fileName,uri,file.getContentType(),file.getSize());
	}
	
	@GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        // Load file as Resource
        Resource resource = fss.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            throw new IOException("Could not load file");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
	

	//pagination:5 users at a time
	@GetMapping("/getUsers/{page}")
	public ResponseEntity<?> getUsers(@PathVariable int page){
		Pageable pg=PageRequest.of(page, 5);
		if(page>=repo.findAllByStatus("Active", pg).getTotalPages())
			return new ResponseEntity<>("No data to display",HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(repo.findAllByStatus("Active",pg),HttpStatus.OK);
	}
	
	@GetMapping("/getUser")
	public loginm getUerId(@RequestParam(name="id") long id) {
		return repo.findById(id);
	}
	@PostMapping("/edit")
	public ResponseEntity<?> edit(@RequestParam(name="id") long userID,@RequestBody signupdto sdto){
		//sdto.setId(userID);
		if(repo.existsById(userID)) {
			loginm lm=repo.findById(userID);
			if(lm.getEmail().equals(sdto.getEmail())) {
				if(sdto.getName().trim()=="")
					return new ResponseEntity<>("Name Required",HttpStatus.BAD_REQUEST);
				if(!(sdto.getName().matches( "[A-Z][a-z]*" )))
		             return new ResponseEntity<>("Enter valid name",HttpStatus.BAD_REQUEST);
				if(sdto.getCpass()==null||sdto.getPass()==null||sdto.getPass().trim()==""||sdto.getCpass().trim()=="")
					return new ResponseEntity<>("Password cannot be empty",HttpStatus.BAD_REQUEST);
				else if(!(sdto.getPass().equals(sdto.getCpass())))
					return new ResponseEntity<>("Passwords do not match",HttpStatus.BAD_REQUEST);
				lm.setName(sdto.getName());
				lm.setPass(sdto.getPass());
				lm.setCpass(sdto.getCpass());
				sdto.setStatus("Active");
				lm.setStatus(sdto.getStatus());
			repo.save(lm);
			return new ResponseEntity<>("Successful!",HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>("Email cannot be changed", HttpStatus.BAD_REQUEST);
			}
		}
		else
			return new ResponseEntity<>("Invalid request",HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/deactivate")
	public ResponseEntity<?> register(@RequestParam(name="id") long userId){
		loginm lm=repo.findById(userId);
		if(lm.getStatus().equals("Active"))
		{lm.setStatus("Deactivated");
		repo.save(lm);
		return new ResponseEntity<>("Successfully Deactivated!",HttpStatus.OK);
		}
		else
			return new ResponseEntity<>("User already Deactivated",HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/UserStatus")
	public ResponseEntity<?> setStatus(@RequestParam(name="id") long userId,@RequestBody status st){
		if(repo.existsById(userId)) {
		st.setActiveStatus("Active");
		st.setId(userId);
		Date date=new Date();
		java.sql.Date sqlDate=new java.sql.Date(date.getTime());
		st.setDate(sqlDate);
		Timestamp ts=new Timestamp(date.getTime());
		st.setTimeStamp(ts);
		//st.setActiveStatus("Active");
		srepo.save(st);
		return new ResponseEntity<>("User Status uploaded sucessfully",HttpStatus.OK);
		}
		else
			return new ResponseEntity<>("User not exist",HttpStatus.BAD_REQUEST);
	}
	@SuppressWarnings("deprecation")
	@GetMapping("/getUserStatus")
	public ResponseEntity<?> getStatus(@RequestParam(name="id")long userId){
		if(srepo.existsById(userId)){
			status s=srepo.findById(userId);
			java.sql.Date d=s.getDate();
			Timestamp ts=s.getTimeStamp();
			Date date=new Date();
			java.sql.Date sqlDate=new java.sql.Date(date.getTime());
			Timestamp t=new Timestamp(date.getTime());
			int compared=d.toString().compareTo(sqlDate.toString());
			if(s.getActiveStatus().equals("Active")) {
			if(compared>0&& sqlDate.getDay()==d.getDay()+1 && sqlDate.getMonth()==d.getMonth() && sqlDate.getYear()==d.getYear()) {
				if(ts.after(t)) {
					return new ResponseEntity<>(srepo.findById(userId),HttpStatus.OK);
				}
				else {
					s.setActiveStatus("Deactivated");
					srepo.save(s);
					return new ResponseEntity<>("Status no longer available 1",HttpStatus.BAD_REQUEST);
				}
			}
			else if(compared==0&& t.after(ts)) {
				return new ResponseEntity<>(srepo.findById(userId),HttpStatus.OK);
			}
			else {
				s.setActiveStatus("Deactivated");
				srepo.save(s);
				return new ResponseEntity<>("Status no longer available 2",HttpStatus.BAD_REQUEST);
			}
			}
			else {
				return new ResponseEntity<>("No Status found",HttpStatus.BAD_REQUEST);
			}
		}
			
		else {
			return new ResponseEntity<>("No Status found",HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody signupdto sdto) {
		if(repo.existsByEmail(sdto.getEmail())) {
			return new ResponseEntity<>("Email already exists!", HttpStatus.BAD_REQUEST);
		}
		if(sdto.getName().trim()=="")
			return new ResponseEntity<>("Name Required",HttpStatus.BAD_REQUEST);
		if(!(sdto.getName().matches( "[A-Z][a-z]*" )))
             return new ResponseEntity<>("Enter valid name",HttpStatus.BAD_REQUEST);
		if(sdto.getEmail().trim()=="")
			return new ResponseEntity<>("Email required",HttpStatus.BAD_REQUEST);
		if(sdto.getCpass()==null||sdto.getPass()==null||sdto.getPass().trim()==""||sdto.getCpass().trim()=="")
			return new ResponseEntity<>("Password cannot be empty",HttpStatus.BAD_REQUEST);
		else if(!(sdto.getPass().equals(sdto.getCpass())))
			return new ResponseEntity<>("Passwords do not match",HttpStatus.BAD_REQUEST);
		
		loginm l=new loginm();
		l.setName(sdto.getName());
		l.setEmail(sdto.getEmail());
		l.setPass(sdto.getPass());
		l.setCpass(sdto.getCpass());
		sdto.setStatus("Active");
		l.setStatus(sdto.getStatus());
		//l.setRole("USER");
		repo.save(l);
		return new ResponseEntity<>("Successful!",HttpStatus.OK);
	}


}
