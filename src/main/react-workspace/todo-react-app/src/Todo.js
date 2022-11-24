import React from 'react';
import DeleteOutlined from "@material-ui/icons/DeleteOutlined";
import { ListItem, ListItemText, InputBase, Checkbox, ListItemSecondaryAction, IconButton } from "@material-ui/core";


class Todo extends React.Component {
    constructor(props) {
        super(props);
        this.state = { item: props.item, readOnly: true};
        this.delete = props.delete;
        this.update = props.update; // update를 this.update에 할당
      }

      // 함수 추가
      deleteEventHandler = () => {
        this.delete(this.state.item)
      }

      offReadOnlyMode = () => {
        console.log("Event!", this.state.readOnly)
        this.setState({ readOnly: false }, () => {
            console.log("ReadOnly? ", this.state.readOnly)
        });
      }

      editEventHandler = (e) => {
        const thisItem = this.state.item;
        thisItem.title = e.target.value;
        this.setState({ item: thisItem });
      }

      enterKeyEventHandler = (e) => {
        if (e.key === "Enter") {
          this.setState( {readOnly: true });
          this.update(this.state.item);
        }
      };

      checkboxEventHandler = (e) => {
        const thisItem = this.state.item;
        thisItem.done = !thisItem.done;
        this.setState({ item: thisItem });
        this.update(this.state.item);
      }

  render() {
    const item = this.state.item;
    return (
        <ListItem>
            <Checkbox checked={item.done} onChange={this.checkboxEventHandler} disableRipple />
            <ListItemText>
                <InputBase 
                    inputProps = {{ "aria-label" : "naked", readOnly: this.state.readOnly, }}
                    type="text"
                    id={item.id}            // 각 리스트를 구분하려고 id를 연결
                    name={item.id}          // 각 리스트를 구분하려고 id를 연결
                    value={item.title}
                    multiline={true}
                    fullWidth={true}
                    onClick={this.offReadOnlyMode}
                    onChange={this.editEventHandler}
                    onKeyPress={this.enterKeyEventHandler}
                />
            </ListItemText>

            <ListItemSecondaryAction>
                <IconButton 
                    aria-label="Delete Todo"
                    onClick={this.deleteEventHandler}>
                    <DeleteOutlined />
                </IconButton>
            </ListItemSecondaryAction>
        </ListItem>
    )
  }
}

export default Todo;