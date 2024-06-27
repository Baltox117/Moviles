import { View, Text, FlatList } from "react-native";
import React, { useState } from "react";
import Btn from "./Components/Btn";
import axios from "axios";

export default function APIs() {
    const [data, setData] = useState([]);
    const baseURL = "https://jsonplaceholder.typicode.com";

    const getAPI = () => {
        axios({
            method: "GET",
            url: `${baseURL}/posts`
        })
        .then(res => console.log(res.data))
        .catch(err => console.log(err));
    };

    const get_by_id = () => {
        axios({
            method: "GET",
            url: `${baseURL}/posts/11`
        })
        .then(res => console.log(res.data))
        .catch(err => console.log(err));
    }

    const postAPI = () => {
        axios({
            method: "POST",
            url: `${baseURL}/posts`,
            body: JSON.stringify({
                id: 101,
                title: "New Title",
                boldy: "New Body for the data",
            })
        }).then(res => console.log(res)).catch(err => console.log(err));
    }

    const patchAPI = () => {
        axios({
            method: "PATCH",
            url: `${baseURL}/posts/16`,
            body: JSON.stringify({
                title: "Updated Title",
            })
        }).then(res => console.log(res)).catch(err => console.log(err));
    }

    const deleteAPI = () => {
        axios({
            method: "DELETE",
            url: `${baseURL}/posts/16`
        }).then(res => console.log(res)).catch(err => console.log(err));
    }

    return (
        <View>
            <Text 
                style={{
                    fontSize: 40,
                    fontWeight: "bold",
                    textAlign: "center",
                    color: "black"
                }}>
                    APIs
                </Text>
            <Btn title="GET" onPress={getAPI}/>
            <Btn title="GET by ID" onPress={get_by_id}/>
            <Btn title="POST" onPress={postAPI}/>
            <Btn title="PATCH" onPress={patchAPI}/>
            <Btn title="DELETE" onPress={deleteAPI}/>
        </View>
    );
}