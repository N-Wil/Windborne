var survey_options = document.getElementById('survey_options');
var add_more_fields = document.getElementById('add_more_fields');
var remove_fields = document.getElementById('remove_fields');
const skills = new Set();
add_more_fields.onclick = function () {
  var newField = document.createElement('input');
  newField.setAttribute('type', 'text');
  newField.setAttribute('name', 'survey_options[]');
  newField.setAttribute('class', 'survey_options');
  newField.setAttribute('size', 50);
  newField.setAttribute('placeholder', 'Skill or Certification');
  survey_options.appendChild(newField);
}

remove_fields.onclick = function () {
  var input_tags = survey_options.getElementsByTagName('input');
  if (input_tags.length > 2) {
    survey_options.removeChild(input_tags[(input_tags.length) - 1]);
  }
}

save_and_return.onclick = function () {
  for (let i = 0; i < survey_options.childElementCount; i++) {
    skills.add(survey_options.children[i].value)
  }
  // var form = document.createElement('form');
  // form.setAttribute('action', "http://localhost:8080/userId/skills");
  // form.setAttribute('method', "POST");
  // inputElement = document.createElement('input');



  var skillArray = [];
  skills.forEach(element => {
    skillArray.push(element);
  });
  // $ajax({
  //   type: "POST",
  //   url: "http://localhost:8080/users/userId/skills",
  //   data: {
  //     skillList: skillArray
  //   },
  //   success: function (response) {
  //     // do something ... 
  //   },
  //   error: function (e) {
  //     alert('Error: ' + e);
  //   }
  // })

  fetch("http://localhost:8080/users/userId/skills", {
    method: 'post',
    body: JSON.stringify(skillArray),
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    }
  }).then((response) => {
    return response.json()
  }).then((res) => {
    if (res.status === 201) {
      console.log("Post successfully created!")
    }
  }).catch((error) => {
    console.log(error)
  })

}
