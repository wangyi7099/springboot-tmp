package org.flowable.ui.modeler.rest.app;

import cn.stylefeng.guns.modular.system.entity.User;
import cn.stylefeng.guns.modular.system.service.UserService;
import org.flowable.ui.common.model.ResultListDataRepresentation;
import org.flowable.ui.common.model.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/app")
public class EditorUsersResource {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/rest/editor-users", method = RequestMethod.GET)
    public ResultListDataRepresentation getUsers(@RequestParam(value = "filter", required = false) String filter) {

        List<User> users = userService.fuzzyUsers(filter);

        List<UserRepresentation> userRepresentations = new ArrayList<>();
        if (users != null && users.size() > 0) {
            for (User u : users) {
                UserRepresentation temp = new UserRepresentation();
                temp.setFirstName(u.getName());
                temp.setId(String.valueOf(u.getUserId()));
                userRepresentations.add(temp);
            }
        }
        return new ResultListDataRepresentation(userRepresentations);
    }

}
